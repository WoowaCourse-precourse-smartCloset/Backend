package precourse.smartcloset.user.service

import org.springframework.stereotype.Service
import precourse.smartcloset.common.Constants.EMAIL_DUPLICATE_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.EMAIL_FORMAT_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.NULL_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.PASSWORD_FORMAT_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.PASSWORD_LENGTH_ERROR_MESSAGE
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class RegisterServiceImpl(
    private val userRepository: UserRepository
) : RegisterService {

    override fun register(email: String, password: String): User {
        validateEmail(email)
        validatePassword(password)
        val user = User(email = email, password = password)
        return userRepository.save(user)
    }

    private fun validateEmail(email: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(email)
//        이메일 형식이 아닌 경우 예외 처리
        validateEmailFormat(email)
//        이메일 중복 체크 (이미 존재하는 경우 예외 처리)
        validateDuplicateEmail(email)
    }

    private fun validatePassword(password: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(password)
//        8자 미만, 12자 초과인 경우 예외 처리
        validatePasswordLength(password)
//        영문, 숫자, 특수문자 중 하나라도 없는 경우 예외 처리
        validatePasswordFormat(password)
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
}