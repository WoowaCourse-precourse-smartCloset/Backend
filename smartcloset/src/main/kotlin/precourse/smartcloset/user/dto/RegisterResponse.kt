package precourse.smartcloset.user.dto

import precourse.smartcloset.user.entity.User
import java.time.LocalDateTime

data class RegisterResponse (
    val userId: Long,
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime
){
    companion object {
        fun from(user: User): RegisterResponse {
            return RegisterResponse(
                userId = user.id!!,
                email = user.email,
                nickname = user.nickname,
                createdAt = user.createdAt
            )
        }
    }
}