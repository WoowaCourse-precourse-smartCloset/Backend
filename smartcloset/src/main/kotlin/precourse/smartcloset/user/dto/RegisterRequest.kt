package precourse.smartcloset.user.dto

data class RegisterRequest (
    val email: String,
    val password: String,
    val confirmPassword: String,
    val nickname: String
)