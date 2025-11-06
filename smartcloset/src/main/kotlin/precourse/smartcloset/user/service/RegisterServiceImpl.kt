package precourse.smartcloset.user.service

import org.springframework.stereotype.Service
import precourse.smartcloset.common.Constants.EMAIL_DUPLICATE_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.EMAIL_FORMAT_ERROR_MESSAGE
import precourse.smartcloset.common.Constants.NULL_ERROR_MESSAGE
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class RegisterServiceImpl(
    private val userRepository: UserRepository
) : RegisterService {

    override fun register(input: String): User {
//        빈 문자열 입력 예외 처리
        validateEmpty(input)
//        이메일 형식이 아닌 경우 예외 처리
        validateEmail(input)
//        이메일 중복 체크 (이미 존재하는 경우 예외 처리)
        validateDuplicateEmail(input)
        val user = User(email = input)
        return userRepository.save(user)
    }

    private fun validateDuplicateEmail(input: String) {
        require(!userRepository.existsByEmail(input)) { EMAIL_DUPLICATE_ERROR_MESSAGE }
    }

    private fun validateEmail(input: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        require(emailRegex.matches(input)) { EMAIL_FORMAT_ERROR_MESSAGE }
    }

    private fun validateEmpty(input: String) {
        require(input.isNotEmpty()) { NULL_ERROR_MESSAGE }
    }
}