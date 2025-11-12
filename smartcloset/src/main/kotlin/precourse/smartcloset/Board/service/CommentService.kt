package precourse.smartcloset.Board.service

import precourse.smartcloset.Board.dto.CommentRequest
import precourse.smartcloset.Board.dto.CommentResponse

interface CommentService {
    fun createComment(userId: Long, boardId: Long, request: CommentRequest): CommentResponse
    fun getCommentsByBoardId(boardId: Long): List<CommentResponse>
    fun updateComment(userId: Long, commentId: Long, request: CommentRequest): CommentResponse
    fun deleteComment(userId: Long, commentId: Long)
}