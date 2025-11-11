package precourse.smartcloset.Board.service

import org.springframework.stereotype.Service
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardResponse
import precourse.smartcloset.Board.entity.Board
import precourse.smartcloset.Board.repository.BoardRepository
import precourse.smartcloset.common.util.Constants.USER_NOT_FOUND_ERROR_MESSAGE
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class BoardServiceImpl(
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository,
    private val validator: Validator
) : BoardService {
    override fun createBoard(userId: Long, request: BoardRequest): BoardResponse {
//        제목 20자 이하
        validator.validateBoardTitle(request.title)
//        내용 100자 이하
        validator.validateBoardContent(request.content)
//        태그 선택, 최대 3개
        validator.validateBoardTags(request.tags)
//        작성자 조회
        val user = findUserById(userId)
//        쉼표로 구분된 문자열로 변환
        val tagsString = buildTagsString(request.tags)
//        게시글 생성
        val board = createBoardEntity(request, user, tagsString)
//        게시글 저장
        val savedBoard = saveBoard(board)
        return BoardResponse.from(savedBoard)
    }

    private fun findUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException(USER_NOT_FOUND_ERROR_MESSAGE) }
    }

    private fun saveBoard(board: Board): Board {
        return boardRepository.save(board)
    }

    private fun createBoardEntity(request: BoardRequest, user: User, tagsString: String?): Board {
        return Board(
            title = request.title,
            content = request.content,
            weather = request.weather,
            imageUrl = request.imageUrl,
            tags = tagsString,
            user = user
        )
    }

    private fun buildTagsString(tags: List<String>?): String? {
        if (tags.isNullOrEmpty()) {
            return null
        }
        val tagsString = tags.joinToString(",")
        return tagsString
    }
}