package precourse.smartcloset.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.BAD_REQUEST_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.INTERNAL_SERVER_ERROR_MESSAGE

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse.error<Nothing>(ex.message ?: BAD_REQUEST_ERROR_MESSAGE)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse.error<Nothing>(INTERNAL_SERVER_ERROR_MESSAGE)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response)
    }
}