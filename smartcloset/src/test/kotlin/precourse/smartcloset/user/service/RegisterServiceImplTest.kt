package precourse.smartcloset.user.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

class RegisterServiceImplTest {
    private val userRepository: UserRepository = mock()
    private val validator: Validator = Validator(userRepository)
    private val passwordEncoder = BCryptPasswordEncoder()
    private val registerService = RegisterServiceImpl(userRepository, validator, passwordEncoder)

    @Test
    fun `회원 가입 성공`() {
//        given
        val email = "test@abc.com"
        val rawPassword = "test123!"
        val nickname = "테스터"

        val savedUser = User(
            id = 1L,
            email = email,
            password = passwordEncoder.encode(rawPassword),
            nickname = nickname
        )

        given(userRepository.existsByEmail(anyString())).willReturn(false)
        given(userRepository.existsByNickname(anyString())).willReturn(false)
        given(userRepository.save(any(User::class.java))).willReturn(savedUser)

        // when
        val result = registerService.register(email, rawPassword, rawPassword, nickname)

        // then
        assertThat(result.id).isEqualTo(savedUser.id)
        assertThat(result.email).isEqualTo(savedUser.email)
        assertThat(result.nickname).isEqualTo(savedUser.nickname)
    }

    @Test
    fun `이메일 중복 실패`() {
        // given
        given(userRepository.existsByEmail("test@example.com")).willReturn(true)

        // when, then
        assertThatThrownBy {
            registerService.register("test@example.com", "test123!@", "test123!@", "테스터")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `비밀번호 불일치 실패`() {
        // given
        given(userRepository.existsByEmail("test@example.com")).willReturn(false)

        // when, then
        assertThatThrownBy {
            registerService.register("test@example.com", "test123!@", "test123@@", "테스터")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `닉네임 중복 실패`() {
        // given
        given(userRepository.existsByEmail("test@example.com")).willReturn(false)
        given(userRepository.existsByNickname("테스터")).willReturn(true)

        // when, then
        assertThatThrownBy {
            registerService.register("test@example.com", "test123!@", "test123!@", "테스터")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `비밀번호 형식 오류 실패`() {
        // given
        given(userRepository.existsByEmail("test@example.com")).willReturn(false)

        // when, then
        assertThatThrownBy {
            registerService.register("test@example.com", "password", "password", "테스터")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `이메일 형식 오류 실패`() {
        // when, then
        assertThatThrownBy {
            registerService.register("test.com", "test123!@", "test123!@", "테스터")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}