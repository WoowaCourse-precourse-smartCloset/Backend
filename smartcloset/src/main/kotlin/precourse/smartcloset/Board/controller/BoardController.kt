package precourse.smartcloset.Board.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import precourse.smartcloset.Board.dto.BoardListResponse
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardResponse
import precourse.smartcloset.Board.dto.BoardUpdateRequest
import precourse.smartcloset.Board.service.BoardService
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.BOARD_CREATE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_GET_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_UPDATE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.SessionUtil

@RestController
@RequestMapping("/api/v1/boards")
class BoardController(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(
        @RequestBody request: BoardRequest,
        session: HttpSession
    ): ResponseEntity<ApiResponse<BoardResponse>> {

        val userId = SessionUtil.getUserId(session)
        val response = boardService.createBoard(userId, request)

        val apiResponse = ApiResponse.success(
            message = BOARD_CREATE_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(apiResponse)
    }

    @GetMapping
    fun getBoardList(
        @RequestParam(required = false) lastId: Long?,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<BoardListResponse>> {

        val response = boardService.getBoardList(lastId, size)

        val apiResponse = ApiResponse.success(
            message = BOARD_GET_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(apiResponse)
    }

    @GetMapping("/{boardId}")
    fun getBoardById(
        @PathVariable boardId: Long
    ): ResponseEntity<ApiResponse<BoardResponse>> {
        val response = boardService.getBoardById(boardId)

        val apiResponse = ApiResponse.success(
            message = BOARD_GET_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(apiResponse)
    }

    @PutMapping("/{boardId}")
    fun updateBoard(
        @PathVariable boardId: Long,
        @RequestBody request: BoardUpdateRequest,
        session: HttpSession
    ): ResponseEntity<ApiResponse<BoardResponse>> {
        val userId = SessionUtil.getUserId(session)
        val response = boardService.updateBoard(userId, boardId, request)

        val apiResponse = ApiResponse.success(
            message = BOARD_UPDATE_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(apiResponse)
    }
}