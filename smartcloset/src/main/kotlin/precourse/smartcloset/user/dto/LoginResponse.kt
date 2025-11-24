package precourse.smartcloset.user.dto

import precourse.smartcloset.user.entity.User

data class LoginResponse(
    val userId: Long,
    val email: String,
    val nickname: String
) {
    companion object {
        fun from(user: User): LoginResponse {
            return LoginResponse(
                userId = user.id!!,
                email = user.email,
                nickname = user.nickname
            )
        }
    }
}