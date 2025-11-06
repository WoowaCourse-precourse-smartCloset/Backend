package precourse.smartcloset.user.service

import precourse.smartcloset.user.entity.User

interface RegisterService {
    fun register(email: String, password: String, confirmPassword: String, nickname: String): User
}