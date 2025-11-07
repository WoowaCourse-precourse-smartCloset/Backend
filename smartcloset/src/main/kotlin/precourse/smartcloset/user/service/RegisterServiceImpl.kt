package precourse.smartcloset.user.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.dto.RegisterResponse
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class RegisterServiceImpl(
    private val userRepository: UserRepository,
    private val validator: Validator,
    private val passwordEncoder: PasswordEncoder
) : RegisterService {

    override fun register(email: String, password: String, confirmPassword: String, nickname: String): RegisterResponse {
//        이메일 검증
        validator.validateEmail(email)
//        비밀번호 검증
        validator.validatePassword(password)
//        비밀번호 확인
        validator.validatePaswordConfirm(password, confirmPassword)
//        닉네임 검증
        validator.validateNickname(nickname)
//        비밀번호 암호화
        val encodedPassword = passwordEncode(password)
//        값 유저에 할당
        val user = User(email = email, password = encodedPassword, nickname = nickname)
//        유저 정보 저장
        val savedUser = saveUser(user)
        return RegisterResponse.from(savedUser)
    }

    private fun passwordEncode(password: String): String {
        return passwordEncoder.encode(password)
    }

    private fun saveUser(user: User): User {
        return userRepository.save(user)
    }
}