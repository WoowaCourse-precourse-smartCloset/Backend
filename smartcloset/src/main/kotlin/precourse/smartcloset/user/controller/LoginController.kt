package precourse.smartcloset.user.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.LOGIN_SUCCESS_MESSAGE
import precourse.smartcloset.common.util.Constants.LOGOUT_SUCCESS_MESSAGE
import precourse.smartcloset.user.dto.LoginRequest
import precourse.smartcloset.user.dto.LoginResponse
import precourse.smartcloset.user.service.LoginService

@RestController
@RequestMapping("/api/v1/users")
class LoginController(private val loginService: LoginService) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest, session: HttpSession): ResponseEntity<ApiResponse<LoginResponse>> {
        val response = loginService.login(
            email = request.email,
            password = request.password
        )

        session.setAttribute("userId", response.userId)

        val apiResponse = ApiResponse.success(
            message = LOGIN_SUCCESS_MESSAGE,
            data = response
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(apiResponse)
    }

    @PostMapping("/logout")
    fun logout(session: HttpSession): ResponseEntity<ApiResponse<Unit>> {
        session.invalidate()

        val apiResponse = ApiResponse.success<Unit>(
            message = LOGOUT_SUCCESS_MESSAGE,
            data = null
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(apiResponse)
    }
}