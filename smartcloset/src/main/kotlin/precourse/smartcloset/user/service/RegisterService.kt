package precourse.smartcloset.user.service

import precourse.smartcloset.user.dto.RegisterResponse

interface RegisterService {
    fun register(email: String, password: String, confirmPassword: String, nickname: String): RegisterResponse
}