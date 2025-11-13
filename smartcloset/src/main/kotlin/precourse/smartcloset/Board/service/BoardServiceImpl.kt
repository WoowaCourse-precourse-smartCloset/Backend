package precourse.smartcloset.Board.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
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
    private val s3FileStorageService: S3FileStorageService,
    private val validator: Validator
) : BoardService {

    @Transactional
    override fun createBoard(userId: Long, request: BoardRequest, imageFile: MultipartFile?): BoardResponse {
        // 유효성 검사
        validator.validateBoardTitle(request.title)
        validator.validateBoardContent(request.content)
        // tags를 String으로 받아서 검증
        val tagsList = convertTagsStringToList(request.tags)
        validator.validateBoardTags(tagsList)
        // 사용자 조회
        val user = findUserById(userId)
        // 이미지 업로드 (선택사항)
        val imageUrl = uploadImage(imageFile)
        // 게시글 생성
        val board = createBoardEntity(request, user, imageUrl)
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

    @Transactional
    override fun updateBoard(
        userId: Long,
        boardId: Long,
        request: BoardUpdateRequest,
        imageFile: MultipartFile?
    ): BoardResponse {
        validator.validateBoardTitle(request.title)
        validator.validateBoardContent(request.content)

        val tagsList = convertTagsStringToList(request.tags)
        validator.validateBoardTags(tagsList)

        val board = findBoardById(boardId)
        validateBoardOwner(board, userId)

        val newImageUrl = updateImageIfProvided(board.imageUrl, imageFile)
        updateBoardEntity(board, request, newImageUrl)

        return BoardResponse.from(board)
    }

    @Transactional
    override fun deleteBoard(userId: Long, boardId: Long) {
        val board = findBoardById(boardId)
        validateBoardOwner(board, userId)

        board.imageUrl?.let { s3FileStorageService.deleteImage(it) }

        boardRepository.delete(board)
    }

    private fun findBoardById(boardId: Long): Board {
        return boardRepository.findByIdOrNull(boardId)
            ?: throw IllegalArgumentException(BOARD_NOT_FOUND_ERROR_MESSAGE)
    }

    private fun findUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException(USER_NOT_FOUND_ERROR_MESSAGE) }
    }

    private fun convertTagsStringToList(tagsString: String?): List<String>? {
        if (tagsString.isNullOrBlank()) return null
        return tagsString.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    private fun uploadImage(imageFile: MultipartFile?): String? {
        if (imageFile == null || imageFile.isEmpty) return null
        return s3FileStorageService.saveImage(imageFile)
    }

    private fun createBoardEntity(request: BoardRequest, user: User, imageUrl: String?): Board {
        return Board(
            title = request.title,
            content = request.content,
            weather = request.weather,
            imageUrl = imageUrl,
            tags = request.tags,
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
        require(board.user.id == userId) { BOARD_UNAUTHORIZED_ERROR_MESSAGE }
    }

    private fun updateBoardEntity(board: Board, request: BoardUpdateRequest, imageUrl: String?) {
        board.update(
            title = request.title,
            content = request.content,
            weather = request.weather,
            imageUrl = imageUrl,
            tags = request.tags
        )
    }

    private fun updateImageIfProvided(currentImageUrl: String?, imageFile: MultipartFile?): String? {
        if (shouldNotUpdateImage(imageFile)) {
            return currentImageUrl
        }

        deleteCurrentImage(currentImageUrl)
        return uploadNewImage(imageFile!!)
    }

    private fun shouldNotUpdateImage(imageFile: MultipartFile?): Boolean {
        return imageFile == null || imageFile.isEmpty
    }

    private fun deleteCurrentImage(imageUrl: String?) {
        imageUrl?.let { s3FileStorageService.deleteImage(it) }
    }

    private fun uploadNewImage(imageFile: MultipartFile): String? {
        return uploadImage(imageFile)
    }
}