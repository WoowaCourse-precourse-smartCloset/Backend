package precourse.smartcloset.common.util

import jakarta.servlet.http.HttpSession
import precourse.smartcloset.common.util.Constants.SESSION_NOT_FOUND_ERROR_MESSAGE

object SessionUtil {
    fun getUserId(session: HttpSession): Long {
        return session.getAttribute("userId") as? Long
            ?: throw IllegalArgumentException(SESSION_NOT_FOUND_ERROR_MESSAGE)
    }
}