package precourse.smartcloset.user.service

import org.springframework.stereotype.Service
import precourse.smartcloset.common.Validator
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class RegisterServiceImpl(
    private val userRepository: UserRepository,
    private val validator: Validator
) : RegisterService {

    override fun register(email: String, password: String, confirmPassword: String, nickname: String): User {
//        이메일 검증
        validator.validateEmail(email)
//        비밀번호 검증
        validator.validatePassword(password)
//        비밀번호 확인
        validator.validatePaswordConfirm(password, confirmPassword)
//        닉네임 검증
        validator.validateNickname(nickname)
        val user = User(email = email, password = password, nickname = nickname)

        return userRepository.save(user)
    }
}