package precourse.smartcloset.user.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import precourse.smartcloset.common.util.Constants.EMAIL_NOT_EXIST_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.PASSWORD_MISMATCH_ERROR_MESSAGE
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.dto.LoginResponse
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class LoginServiceImpl(
    private val validator: Validator,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : LoginService {
    override fun login(email: String, password: String): LoginResponse {
        validator.validateLoginEmail(email)
        validator.validateEmpty(password)
        val user = findUserByEmail(email)
        validateLoginPassword(password, user.password)
        return LoginResponse.from(user)
    }

    private fun validateLoginPassword(input: String, password: String) {
        require(passwordEncoder.matches(input, password)) {
            PASSWORD_MISMATCH_ERROR_MESSAGE
        }
    }

    private fun findUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            ?: throw IllegalArgumentException(EMAIL_NOT_EXIST_ERROR_MESSAGE)
    }
}