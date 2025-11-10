package precourse.smartcloset.user.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import precourse.smartcloset.common.util.Constants.LOGIN_REQUIRED_ERROR_MESSAGE

@Component
class SessionAuthInterceptor : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val session = request.getSession(false)
            ?: throw IllegalArgumentException(LOGIN_REQUIRED_ERROR_MESSAGE)

        return true
    }
}