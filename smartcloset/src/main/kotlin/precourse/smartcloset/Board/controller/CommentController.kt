package precourse.smartcloset.Board.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import precourse.smartcloset.Board.dto.CommentRequest
import precourse.smartcloset.Board.dto.CommentResponse
import precourse.smartcloset.Board.service.CommentService
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.COMMENT_CREATE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.COMMENT_DELETE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.COMMENT_GET_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.COMMENT_UPDATE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.SessionUtil

@CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/boards/{boardId}/comments")
class CommentController(private val commentService: CommentService) {

    @PostMapping
    fun createComment(
        @PathVariable boardId: Long,
        @RequestBody request: CommentRequest,
        session: HttpSession
    ): ResponseEntity<ApiResponse<CommentResponse>> {
        val userId = SessionUtil.getUserId(session)
        val response = commentService.createComment(userId, boardId, request)

        return createSuccessResponse(response, COMMENT_CREATE_SUCCESS_MESSAGE, HttpStatus.CREATED)
    }

    @GetMapping
    fun getCommentsByBoardId(
        @PathVariable boardId: Long
    ): ResponseEntity<ApiResponse<List<CommentResponse>>> {
        val response = commentService.getCommentsByBoardId(boardId)
        return createSuccessResponse(response, COMMENT_GET_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable boardId: Long,
        @PathVariable commentId: Long,
        @RequestBody request: CommentRequest,
        session: HttpSession
    ): ResponseEntity<ApiResponse<CommentResponse>> {
        val userId = SessionUtil.getUserId(session)
        val response = commentService.updateComment(userId, commentId, request)

        return createSuccessResponse(response, COMMENT_UPDATE_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable boardId: Long,
        @PathVariable commentId: Long,
        session: HttpSession
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId = SessionUtil.getUserId(session)
        commentService.deleteComment(userId, commentId)

        return createSuccessResponse(null, COMMENT_DELETE_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    private fun <T> createSuccessResponse(
        data: T?,
        message: String,
        status: HttpStatus
    ): ResponseEntity<ApiResponse<T>> {
        val apiResponse = ApiResponse.success(message = message, data = data)
        return ResponseEntity.status(status).body(apiResponse)
    }
}