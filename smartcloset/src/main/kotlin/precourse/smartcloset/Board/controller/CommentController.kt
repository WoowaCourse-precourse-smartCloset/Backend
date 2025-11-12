package precourse.smartcloset.Board.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import precourse.smartcloset.Board.dto.CommentRequest
import precourse.smartcloset.Board.dto.CommentResponse
import precourse.smartcloset.Board.service.CommentService
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.COMMENT_CREATE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.COMMENT_GET_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.SessionUtil

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

        val apiResponse = ApiResponse.success(
            message = COMMENT_CREATE_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(apiResponse)
    }

    @GetMapping
    fun getCommentsByBoardId(
        @PathVariable boardId: Long
    ): ResponseEntity<ApiResponse<List<CommentResponse>>> {
        val response = commentService.getCommentsByBoardId(boardId)

        val apiResponse = ApiResponse.success(
            message = COMMENT_GET_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(apiResponse)
    }

}