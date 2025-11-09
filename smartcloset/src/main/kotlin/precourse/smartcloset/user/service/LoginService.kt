package precourse.smartcloset.user.service

interface LoginService {
    fun login(email: String): Boolean
}