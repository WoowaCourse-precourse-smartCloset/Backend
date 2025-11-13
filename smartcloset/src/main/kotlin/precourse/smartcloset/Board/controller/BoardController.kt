package precourse.smartcloset.Board.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import precourse.smartcloset.Board.dto.BoardListResponse
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardResponse
import precourse.smartcloset.Board.dto.BoardUpdateRequest
import precourse.smartcloset.Board.entity.WeatherType
import precourse.smartcloset.Board.service.BoardService
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.BOARD_CREATE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_DELETE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_GET_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_UPDATE_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.INVALID_WEATHER_TYPE_ERROR_MESSAGE
import precourse.smartcloset.common.util.SessionUtil

@CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/boards")
class BoardController(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(
        @RequestParam title: String,
        @RequestParam content: String,
        @RequestParam weather: String,
        @RequestParam(required = false) tags: String?,
        @RequestParam(required = false) imageFile: MultipartFile?,
        session: HttpSession
    ): ResponseEntity<ApiResponse<BoardResponse>> {
        val userId = SessionUtil.getUserId(session)
        val weatherType = parseWeatherType(weather)
        val request = createBoardRequest(title, content, weatherType, tags)
        val response = boardService.createBoard(userId, request, imageFile)

        return createSuccessResponse(response, BOARD_CREATE_SUCCESS_MESSAGE, HttpStatus.CREATED)
    }

    @GetMapping
    fun getBoardList(
        @RequestParam(required = false) lastId: Long?,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<BoardListResponse>> {
        val response = boardService.getBoardList(lastId, size)
        return createSuccessResponse(response, BOARD_GET_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    @GetMapping("/{boardId}")
    fun getBoardById(@PathVariable boardId: Long): ResponseEntity<ApiResponse<BoardResponse>> {
        val response = boardService.getBoardById(boardId)
        return createSuccessResponse(response, BOARD_GET_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    @PutMapping("/{boardId}")
    fun updateBoard(
        @PathVariable boardId: Long,
        @RequestBody request: BoardUpdateRequest,
        session: HttpSession
    ): ResponseEntity<ApiResponse<BoardResponse>> {
        val userId = SessionUtil.getUserId(session)
        val response = boardService.updateBoard(userId, boardId, request, null)

        return createSuccessResponse(response, BOARD_UPDATE_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    @PutMapping("/{boardId}/with-image")
    fun updateBoardWithImage(
        @PathVariable boardId: Long,
        @RequestParam title: String,
        @RequestParam content: String,
        @RequestParam weather: String,
        @RequestParam(required = false) tags: String?,
        @RequestParam(required = false) imageFile: MultipartFile?,
        session: HttpSession
    ): ResponseEntity<ApiResponse<BoardResponse>> {
        val userId = SessionUtil.getUserId(session)
        val weatherType = parseWeatherType(weather)
        val request = createBoardUpdateRequest(title, content, weatherType, tags)
        val response = boardService.updateBoard(userId, boardId, request, imageFile)

        return createSuccessResponse(response, BOARD_UPDATE_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    @DeleteMapping("/{boardId}")
    fun deleteBoard(
        @PathVariable boardId: Long,
        session: HttpSession
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId = SessionUtil.getUserId(session)
        boardService.deleteBoard(userId, boardId)

        return createSuccessResponse(null, BOARD_DELETE_SUCCESS_MESSAGE, HttpStatus.OK)
    }

    private fun parseWeatherType(weather: String): WeatherType {
        return runCatching {
            WeatherType.valueOf(weather.uppercase())
        }.getOrElse {
            throw IllegalArgumentException(INVALID_WEATHER_TYPE_ERROR_MESSAGE)
        }
    }

    private fun createBoardRequest(
        title: String,
        content: String,
        weather: WeatherType,
        tags: String?
    ): BoardRequest {
        return BoardRequest(
            title = title,
            content = content,
            weather = weather,
            tags = tags
        )
    }

    private fun createBoardUpdateRequest(
        title: String,
        content: String,
        weather: WeatherType,
        tags: String?
    ): BoardUpdateRequest {
        return BoardUpdateRequest(
            title = title,
            content = content,
            weather = weather,
            tags = tags
        )
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