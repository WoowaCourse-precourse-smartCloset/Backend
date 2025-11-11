package precourse.smartcloset.Board.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import precourse.smartcloset.Board.dto.BoardListResponse
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardResponse
import precourse.smartcloset.Board.dto.BoardUpdateRequest
import precourse.smartcloset.Board.entity.Board
import precourse.smartcloset.Board.repository.BoardRepository
import precourse.smartcloset.common.util.Constants.BOARD_NOT_FOUND_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.BOARD_UNAUTHORIZED_ERROR_MESSAGE
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
        val tagsString = convertTagsToString(request.tags)
//        게시글 생성
        val board = createBoardEntity(request, user, tagsString)
//        게시글 저장
        val savedBoard = boardRepository.save(board)
        return BoardResponse.from(savedBoard)
    }

    @Transactional(readOnly = true)
    override fun getBoardList(lastId: Long?, size: Int): BoardListResponse {
        val boards = fetchBoards(lastId, size)
        val hasNext = hasNextPage(boards, size)
        val boardList = extractBoardList(boards, hasNext)
        val boardResponses = convertToBoardResponses(boardList)
        val nextLastId = calculateNextLastId(boardList, hasNext)

        return createBoardListResponse(boardResponses, hasNext, nextLastId)
    }

    @Transactional(readOnly = true)
    override fun getBoardById(boardId: Long): BoardResponse {
        val board = findBoardById(boardId)
        return BoardResponse.from(board)
    }

    override fun updateBoard(userId: Long, boardId: Long, request: BoardUpdateRequest): BoardResponse {
        validator.validateBoardTitle(request.title)
        validator.validateBoardContent(request.content)
        validator.validateBoardTags(request.tags)

        val board = findBoardById(boardId)
        validateBoardOwner(board, userId)

        val tagsString = convertTagsToString(request.tags)
        updateBoardEntity(board, request, tagsString)

        return BoardResponse.from(board)
    }

    private fun findBoardById(boardId: Long) =
        boardRepository.findByIdOrNull(boardId)
            ?: throw IllegalArgumentException(BOARD_NOT_FOUND_ERROR_MESSAGE)

    private fun findUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException(USER_NOT_FOUND_ERROR_MESSAGE) }
    }

    private fun convertTagsToString(tags: List<String>?): String? {
        return tags?.joinToString(",")
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

    private fun fetchBoards(lastId: Long?, size: Int): List<Board> {
        val pageable = PageRequest.of(0, size + 1)
        if (lastId == null) return boardRepository.findAllWithUser(pageable)
        return boardRepository.findByIdLessThanWithUser(lastId, pageable)
    }

    private fun hasNextPage(boards: List<Board>, size: Int): Boolean {
        return boards.size > size
    }

    private fun extractBoardList(boards: List<Board>, hasNext: Boolean): List<Board> {
        if (hasNext) return boards.dropLast(1)
        return boards
    }

    private fun convertToBoardResponses(boards: List<Board>): List<BoardResponse> {
        return boards.map { BoardResponse.from(it) }
    }

    private fun calculateNextLastId(boards: List<Board>, hasNext: Boolean): Long? {
        if (!hasNext) return null
        if (boards.isEmpty()) return null
        return boards.last().id
    }

    private fun createBoardListResponse(
        boards: List<BoardResponse>,
        hasNext: Boolean,
        nextLastId: Long?
    ): BoardListResponse {
        return BoardListResponse(
            boards = boards,
            hasNext = hasNext,
            nextLastId = nextLastId
        )
    }

    private fun validateBoardOwner(board: Board, userId: Long) {
        require(board.user.id != userId) { BOARD_UNAUTHORIZED_ERROR_MESSAGE }
    }

    private fun updateBoardEntity(board: Board, request: BoardUpdateRequest, tagsString: String?) {
        board.update(
            title = request.title,
            content = request.content,
            weather = request.weather,
            imageUrl = request.imageUrl,
            tags = tagsString
        )
    }
}