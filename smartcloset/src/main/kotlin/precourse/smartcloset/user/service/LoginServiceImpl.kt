package precourse.smartcloset.user.service

import org.springframework.stereotype.Service
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.dto.LoginResponse

@Service
class LoginServiceImpl(
    private val validator: Validator
): LoginService {
    override fun login(email: String, password: String): LoginResponse {
        validator.validateEmail(email)
        validator.validatePassword(password)
    }
}