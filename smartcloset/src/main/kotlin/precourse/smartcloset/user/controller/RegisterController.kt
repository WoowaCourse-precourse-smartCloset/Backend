package precourse.smartcloset.user.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.LOGIN_REQUIRED_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.REGISTER_SUCCESS_MESSAGE
import precourse.smartcloset.user.dto.RegisterRequest
import precourse.smartcloset.user.dto.RegisterResponse
import precourse.smartcloset.user.service.RegisterService

@RestController
@RequestMapping("/api/v1/users")
class RegisterController(private val registerService: RegisterService) {
    //    회원가입
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<RegisterResponse>> {
        val response = registerService.register(
            email = request.email,
            password = request.password,
            confirmPassword = request.confirmPassword,
            nickname = request.nickname
        )

        val apiResponse = ApiResponse.success(
            message = REGISTER_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(apiResponse)
    }

    @DeleteMapping("/withdraw")
    fun withdraw(session: HttpSession): ResponseEntity<ApiResponse<Unit>> {

        val userId = session.getAttribute("userId") as Long?
            ?: throw IllegalArgumentException(LOGIN_REQUIRED_ERROR_MESSAGE)

        registerService.withdraw(userId)
    }
}