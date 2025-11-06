package precourse.smartcloset.user.service

import org.springframework.stereotype.Service
import precourse.smartcloset.common.Constants.EMAIL_DUPLICATE_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.EMAIL_FORMAT_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.NICKNAME_DUPLICATE_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.NICKNAME_LENGTH_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.NULL_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.PASSWORD_FORMAT_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.PASSWORD_LENGTH_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.PASSWORD_MISMATCH_ERROR_MESSAGE
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class RegisterServiceImpl(
    private val userRepository: UserRepository
) : RegisterService {

    override fun register(email: String, password: String, confirmPassword: String, nickname: String): User {
//        이메일 검증
        validateEmail(email)
//        비밀번호 검증
        validatePassword(password)
//        비밀번호 확인
        validatePaswordConfirm(password, confirmPassword)
//        닉네임 검증
        validateNickname(nickname)
        val user = User(email = email, password = password, nickname = nickname)

        return userRepository.save(user)
    }

//    이메일
    private fun validateEmail(email: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(email)
//        이메일 형식이 아닌 경우 예외 처리
        validateEmailFormat(email)
//        이메일 중복 체크 (이미 존재하는 경우 예외 처리)
        validateDuplicateEmail(email)
    }

//    비밀번호
    private fun validatePassword(password: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(password)
//        8자 미만, 12자 초과인 경우 예외 처리
        validatePasswordLength(password)
//        영문, 숫자, 특수문자 중 하나라도 없는 경우 예외 처리
        validatePasswordFormat(password)
    }

//    비밀번호 검증
    private fun validatePaswordConfirm(password: String, confirmPassword: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(confirmPassword)
//        비밀번호와 일치하지 않는 경우 예외 처리
        validatePasswordMismatch(password, confirmPassword)
    }

//    닉네임
    private fun validateNickname(nickname: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(nickname)
//        2자 미만, 8자 초과인 경우 예외 처리
        validateNicknameLength(nickname)
//        닉네임 중복 체크 (이미 존재하는 경우 예외 처리)
        validateDuplicateNickname(nickname)
    }

    private fun validateDuplicateEmail(email: String) {
        require(!userRepository.existsByEmail(email)) { EMAIL_DUPLICATE_ERROR_MESSAGE }
    }

    private fun validateEmailFormat(email: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        require(emailRegex.matches(email)) { EMAIL_FORMAT_ERROR_MESSAGE }
    }

    private fun validateEmpty(input: String) {
        require(input.isNotEmpty()) { NULL_ERROR_MESSAGE }
    }

    private fun validatePasswordLength(password: String) {
        require(password.length in 8..12) { PASSWORD_LENGTH_ERROR_MESSAGE }
    }

    private fun validatePasswordFormat(password: String) {
        val hasLetter = checkLetter(password)
        val hasDigit = checkDigit(password)
        val hasSpecial = checkSpecial(password)

        require(hasLetter && hasDigit && hasSpecial) { PASSWORD_FORMAT_ERROR_MESSAGE }
    }

    private fun checkLetter(password: String): Boolean {
        return password.any { it.isLetter() }
    }

    private fun checkDigit(password: String): Boolean {
        return password.any { it.isDigit() }
    }

    private fun checkSpecial(password: String): Boolean {
        return password.any { !it.isLetterOrDigit() }
    }

    private fun validatePasswordMismatch(password: String, confirmPassword: String) {
        require(password == confirmPassword) { PASSWORD_MISMATCH_ERROR_MESSAGE }
    }

    private fun validateNicknameLength(nickname: String) {
        require(nickname.length in 2..8) { NICKNAME_LENGTH_ERROR_MESSAGE }
    }

    private fun validateDuplicateNickname(nickname: String) {
        require(!userRepository.existsByNickname(nickname)) { NICKNAME_DUPLICATE_ERROR_MESSAGE }
    }
}