package precourse.smartcloset.common.util

import org.springframework.stereotype.Component
import precourse.smartcloset.common.util.Constants.BOARD_CONTENT_LENGTH_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_TAGS_COUNT_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_TAG_LENGTH_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_TITLE_LENGTH_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.COMMENT_CONTENT_LENGTH_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.EMAIL_NOT_EXIST_ERROR_MESSAGE
import precourse.smartcloset.user.repository.UserRepository

@Component
class Validator(
    private val userRepository: UserRepository) {

//    이메일 ( 회원가입 )
    fun validateRegisterEmail(email: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(email)
//        이메일 형식이 아닌 경우 예외 처리
        validateEmailFormat(email)
//        이메일 중복 체크 (이미 존재하는 경우 예외 처리)
        validateDuplicateEmail(email)
    }

//    비밀번호
    fun validateRegisterPassword(password: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(password)
//        8자 미만, 12자 초과인 경우 예외 처리
        validatePasswordLength(password)
//        영문, 숫자, 특수문자 중 하나라도 없는 경우 예외 처리
        validatePasswordFormat(password)
    }

//    비밀번호 검증
    fun validatePaswordConfirm(password: String, confirmPassword: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(confirmPassword)
//        비밀번호와 일치하지 않는 경우 예외 처리
        validatePasswordMismatch(password, confirmPassword)
    }

//    닉네임
    fun validateNickname(nickname: String) {
//        빈 문자열 입력 예외 처리
        validateEmpty(nickname)
//        2자 미만, 8자 초과인 경우 예외 처리
        validateNicknameLength(nickname)
//        닉네임 중복 체크 (이미 존재하는 경우 예외 처리)
        validateDuplicateNickname(nickname)
    }

    //    이메일 예외 ( 로그인 )
    fun validateLoginEmail(email: String) {
        validateEmpty(email)
        validateExistEmail(email)
    }

    fun validateBoardTitle(title: String) {
        validateEmpty(title)
        require(title.length <= 20) { BOARD_TITLE_LENGTH_ERROR_MESSAGE }
    }

    // 게시글 내용 검증
    fun validateBoardContent(content: String) {
        validateEmpty(content)
        require(content.length <= 100) { BOARD_CONTENT_LENGTH_ERROR_MESSAGE }
    }

    // 게시글 태그 검증
    fun validateBoardTags(tags: List<String>?) {
        if (tags == null) return
        require(tags.size <= 3) { BOARD_TAGS_COUNT_ERROR_MESSAGE }
        tags.forEach { tag ->
            require(tag.length <= 10) { BOARD_TAG_LENGTH_ERROR_MESSAGE }
        }
    }

    //    댓글 내용 검증
    fun validateCommentContent(content: String) {
        validateEmpty(content)
        require(content.length <= 100) { COMMENT_CONTENT_LENGTH_ERROR_MESSAGE }
    }

    private fun validateExistEmail(email: String) {
        require(userRepository.existsByEmail(email)) { EMAIL_NOT_EXIST_ERROR_MESSAGE }
    }

    private fun validateDuplicateEmail(email: String) {
        require(!userRepository.existsByEmail(email)) { Constants.EMAIL_DUPLICATE_ERROR_MESSAGE }
    }

    private fun validateEmailFormat(email: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        require(emailRegex.matches(email)) { Constants.EMAIL_FORMAT_ERROR_MESSAGE }
    }

    fun validateEmpty(input: String) {
        require(input.isNotEmpty()) { Constants.NULL_ERROR_MESSAGE }
    }

    private fun validatePasswordLength(password: String) {
        require(password.length in 8..12) { Constants.PASSWORD_LENGTH_ERROR_MESSAGE }
    }

    private fun validatePasswordFormat(password: String) {
        val hasLetter = checkLetter(password)
        val hasDigit = checkDigit(password)
        val hasSpecial = checkSpecial(password)

        require(hasLetter && hasDigit && hasSpecial) { Constants.PASSWORD_FORMAT_ERROR_MESSAGE }
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
        require(password == confirmPassword) { Constants.PASSWORD_MISMATCH_ERROR_MESSAGE }
    }

    private fun validateNicknameLength(nickname: String) {
        require(nickname.length in 2..8) { Constants.NICKNAME_LENGTH_ERROR_MESSAGE }
    }

    private fun validateDuplicateNickname(nickname: String) {
        require(!userRepository.existsByNickname(nickname)) { Constants.NICKNAME_DUPLICATE_ERROR_MESSAGE }
    }
}