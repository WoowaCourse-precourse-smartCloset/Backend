package precourse.smartcloset.user.dto

data class RegisterRequest (
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val nickname: String
)