package precourse.smartcloset.common.util

object Constants {
//    아이디
    const val NULL_ERROR_MESSAGE = "[ERROR] 이메일은 빈 값일 수 없습니다."
    const val EMAIL_FORMAT_ERROR_MESSAGE = "[ERROR] 올바른 이메일 형식이 아닙니다."
    const val EMAIL_DUPLICATE_ERROR_MESSAGE = "[ERROR] 이미 존재하는 이메일입니다."
//    패스워드
    const val PASSWORD_LENGTH_ERROR_MESSAGE = "[ERROR] 비밀번호는 8자 이상 12자 이하여야 합니다."
    const val PASSWORD_FORMAT_ERROR_MESSAGE = "[ERROR] 비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
//    패스워드 확인
    const val PASSWORD_MISMATCH_ERROR_MESSAGE = "[ERROR] 비밀번호가 일치하지 않습니다."
//    닉네임
    const val NICKNAME_LENGTH_ERROR_MESSAGE = "[ERROR] 닉네임은 2자 이상 8자 이하여야 합니다."
    const val NICKNAME_DUPLICATE_ERROR_MESSAGE = "[ERROR] 이미 존재하는 닉네임입니다."
//    공통예외
    const val BAD_REQUEST_ERROR_MESSAGE = "[ERROR] 잘못된 요청입니다."
    const val INTERNAL_SERVER_ERROR_MESSAGE = "[ERROR] 서버 오류가 발생했습니다."
//    회원가입 성공 메시지
    const val REGISTER_SUCCESS_MESSAGE = "회원가입이 완료되었습니다."
//    로그인
    const val LOGIN_REQUIRED_ERROR_MESSAGE = "[ERROR] 로그인이 필요합니다."
    const val EMAIL_NOT_EXIST_ERROR_MESSAGE = "[ERROR] 존재하지 않는 아이디입니다."
    const val LOGIN_SUCCESS_MESSAGE = "로그인에 성공하였습니다."
    const val LOGOUT_SUCCESS_MESSAGE = "로그아웃에 성공하엿습니다."
    const val WITHDRAW_SUCCESS_MESSAGE = "회원탈퇴에 성공하였습니다. 이용해주셔서 감사합니다."
}