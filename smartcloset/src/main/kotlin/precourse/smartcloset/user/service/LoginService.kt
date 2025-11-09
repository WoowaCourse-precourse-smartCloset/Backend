package precourse.smartcloset.user.service

import precourse.smartcloset.user.dto.LoginResponse

interface LoginService {
    fun login(email: String, password: String): LoginResponse
}