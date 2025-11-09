package precourse.smartcloset.user.service

import org.springframework.stereotype.Service
import precourse.smartcloset.common.util.Validator

@Service
class LoginServiceImpl(
    private val validator: Validator
): LoginService {
    override fun login(email: String): Boolean {
        validator.validateEmail(email)
    }
}