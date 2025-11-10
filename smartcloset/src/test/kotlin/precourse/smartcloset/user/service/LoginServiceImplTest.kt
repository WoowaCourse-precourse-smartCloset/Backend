package precourse.smartcloset.user.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

class LoginServiceImplTest {
    private val userRepository: UserRepository = mock()
    private val validator: Validator = Validator(userRepository)
    private val passwordEncoder = BCryptPasswordEncoder()
    private val loginService = LoginServiceImpl(validator, userRepository, passwordEncoder)

    @Test
    fun `로그인 성공`() {
        val email = "test@abc.com"
        val rawPassword = "test123!"
        val encodedPassword = passwordEncoder.encode(rawPassword)

        val user = User(
            id = 1L,
            email = email,
            password = encodedPassword,
            nickname = "테스터"
        )

        given(userRepository.existsByEmail(email)).willReturn(true)
        given(userRepository.findByEmail(email)).willReturn(user)

        val result = loginService.login(email, rawPassword)

        assertThat(result.userId).isEqualTo(user.id)
        assertThat(result.email).isEqualTo(user.email)
        assertThat(result.nickname).isEqualTo(user.nickname)
    }

    @Test
    fun `이메일이 존재하지 않는 경우 예외 발생`() {

        val email = "notfound@abc.com"
        val password = "test123!"

        given(userRepository.existsByEmail(email)).willReturn(false)


        assertThatThrownBy {
            loginService.login(email, password)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `비밀번호가 빈 문자열인 경우 예외 발생`() {

        val email = "test@abc.com"
        val emptyPassword = ""

        given(userRepository.existsByEmail(email)).willReturn(true)

        assertThatThrownBy {
            loginService.login(email, emptyPassword)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `비밀번호 불일치 시 예외 발생`() {

        val email = "test@abc.com"
        val correctPassword = "correct123!"
        val wrongPassword = "wrong123!"
        val encodedPassword = passwordEncoder.encode(correctPassword)

        val user = User(
            id = 1L,
            email = email,
            password = encodedPassword,
            nickname = "테스터"
        )

        given(userRepository.existsByEmail(email)).willReturn(true)
        given(userRepository.findByEmail(email)).willReturn(user)

        assertThatThrownBy {
            loginService.login(email, wrongPassword)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}