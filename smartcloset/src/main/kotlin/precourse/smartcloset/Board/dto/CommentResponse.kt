package precourse.smartcloset.Board.dto

import precourse.smartcloset.Board.entity.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val content: String,
    val userId: Long,
    val nickname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(comment: Comment): CommentResponse {
            return CommentResponse(
                commentId = comment.id!!,
                content = comment.content,
                userId = comment.user.id!!,
                nickname = comment.user.nickname,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}