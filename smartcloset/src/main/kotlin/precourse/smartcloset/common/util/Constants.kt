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
    // 게시글
    const val BOARD_TITLE_LENGTH_ERROR_MESSAGE = "[ERROR] 제목은 20자 이하여야 합니다."
    const val BOARD_CONTENT_LENGTH_ERROR_MESSAGE = "[ERROR] 내용은 100자 이하여야 합니다."
    const val BOARD_TAGS_SIZE_ERROR_MESSAGE = "[ERROR] 태그는 최대 3개까지 등록할 수 있습니다."
    const val BOARD_NOT_FOUND_ERROR_MESSAGE = "[ERROR] 게시글을 찾을 수 없습니다."
    const val BOARD_CREATE_SUCCESS_MESSAGE = "게시글이 작성되었습니다."
    const val BOARD_GET_SUCCESS_MESSAGE = "게시글 목록을 조회하였습니다."
    const val BOARD_UNAUTHORIZED_ERROR_MESSAGE = "[ERROR] 게시글을 수정/삭제할 권한이 없습니다."
    const val USER_NOT_FOUND_ERROR_MESSAGE = "[ERROR] 존재하지 않는 유저입니다."
    const val SESSION_NOT_FOUND_ERROR_MESSAGE = "[ERROR] 세션 시간이 종료되었습니다. 다시 로그인 해주세요."
    const val BOARD_UPDATE_SUCCESS_MESSAGE = "게시글이 수정되었습니다."
    const val BOARD_DELETE_SUCCESS_MESSAGE = "게시글이 삭제되었습니다."
}