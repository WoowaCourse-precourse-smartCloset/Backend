package precourse.smartcloset.Board.dto

import precourse.smartcloset.Board.entity.Board
import precourse.smartcloset.Board.entity.WeatherType
import java.time.LocalDateTime

data class BoardResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val weather: WeatherType,
    val imageUrl: String?,
    val tags: List<String>?,
    val userId: Long,
    val nickname: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(board: Board): BoardResponse {
            return BoardResponse(
                boardId = board.id!!,
                title = board.title,
                content = board.content,
                weather = board.weather,
                imageUrl = board.imageUrl,
                tags = board.tags?.split(",")?.map { it.trim() },
                userId = board.user.id!!,
                nickname = board.user.nickname,
                createdAt = board.createdAt,
                updatedAt = board.updatedAt
            )
        }
    }
}