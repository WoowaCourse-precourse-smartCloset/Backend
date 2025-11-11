package precourse.smartcloset.board.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.springframework.data.domain.PageRequest
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardUpdateRequest
import precourse.smartcloset.Board.entity.Board
import precourse.smartcloset.Board.entity.WeatherType
import precourse.smartcloset.Board.repository.BoardRepository
import precourse.smartcloset.Board.service.BoardServiceImpl
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository
import java.util.*

class BoardServiceImplTest {
    private val boardRepository: BoardRepository = mock()
    private val userRepository: UserRepository = mock()
    private val validator: Validator = mock()
    private val boardService = BoardServiceImpl(boardRepository, userRepository, validator)

    @Test
    fun `게시글 작성 성공`() {
        val userId = 1L
        val request = BoardRequest(
            title = "테스트 제목",
            content = "테스트 내용",
            weather = WeatherType.SUNNY,
            imageUrl = "https://test.jpg",
            tags = listOf("태그1", "태그2")
        )

        val user = User(
            id = userId,
            email = "test@test.com",
            password = "test123!",
            nickname = "테스터"
        )

        val board = Board(
            id = 1L,
            title = request.title,
            content = request.content,
            weather = request.weather,
            imageUrl = request.imageUrl,
            tags = "태그1,태그2",
            user = user
        )

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(boardRepository.save(any())).willReturn(board)

        val result = boardService.createBoard(userId, request)

        assertThat(result.boardId).isEqualTo(1L)
        assertThat(result.title).isEqualTo("테스트 제목")
        assertThat(result.nickname).isEqualTo("테스터")
        assertThat(result.tags).containsExactly("태그1", "태그2")
    }

    @Test
    fun `사용자가 존재하지 않으면 예외 발생`() {
        val userId = 999L
        val request = BoardRequest(
            title = "테스트",
            content = "테스트 내용",
            weather = WeatherType.SUNNY
        )

        given(userRepository.findById(userId)).willReturn(Optional.empty())

        assertThatThrownBy {
            boardService.createBoard(userId, request)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `게시글 목록 조회 성공 - lastId 없음`() {
        val user = User(1L, "test@test.com", "test123!", "테스터")
        val boards = listOf(
            Board(3L, "제목3", "내용3", WeatherType.SUNNY, null, null, user),
            Board(2L, "제목2", "내용2", WeatherType.CLOUDY, null, null, user),
            Board(1L, "제목1", "내용1", WeatherType.RAINY, null, null, user)
        )

        val pageable = PageRequest.of(0, 11)
        given(boardRepository.findAllWithUser(pageable)).willReturn(boards)

        val result = boardService.getBoardList(null, 10)

        assertThat(result.boards).hasSize(3)
        assertThat(result.hasNext).isFalse()
        assertThat(result.nextLastId).isNull()
    }

    @Test
    fun `게시글 목록 조회 성공 - lastId 있음 및 hasNext true`() {
        val user = User(1L, "test@test.com", "test123!", "테스터")
        val boards = (1..11).map {
            Board(it.toLong(), "제목$it", "내용$it", WeatherType.SUNNY, null, null, user)
        }

        val pageable = PageRequest.of(0, 11)
        given(boardRepository.findByIdLessThanWithUser(100L, pageable)).willReturn(boards)

        val result = boardService.getBoardList(100L, 10)

        assertThat(result.boards).hasSize(10)
        assertThat(result.hasNext).isTrue()
        assertThat(result.nextLastId).isEqualTo(10L)
    }

    @Test
    fun `게시글 상세 조회 성공`() {
        val user = User(1L, "test@test.com", "test123!", "테스터")
        val board = Board(
            id = 1L,
            title = "테스트 제목",
            content = "테스트 내용",
            weather = WeatherType.SUNNY,
            imageUrl = "https://test.jpg",
            tags = "태그1,태그2",
            user = user
        )

        given(boardRepository.findById(1L)).willReturn(Optional.of(board))


        val result = boardService.getBoardById(1L)

        assertThat(result.boardId).isEqualTo(1L)
        assertThat(result.title).isEqualTo("테스트 제목")
        assertThat(result.nickname).isEqualTo("테스터")
    }

    @Test
    fun `존재하지 않는 게시글 조회 시 예외 발생`() {
        given(boardRepository.findById(999L)).willReturn(Optional.empty())

        assertThatThrownBy {
            boardService.getBoardById(999L)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `게시글 수정 성공`() {
        val userId = 1L
        val boardId = 1L
        val request = BoardUpdateRequest(
            title = "수정된 제목",
            content = "수정된 내용",
            weather = WeatherType.CLOUDY,
            imageUrl = "https://updated.jpg",
            tags = listOf("수정태그")
        )

        val user = User(userId, "test@test.com", "test123!", "테스터")
        val board = Board(
            id = boardId,
            title = "원래 제목",
            content = "원래 내용",
            weather = WeatherType.SUNNY,
            imageUrl = "https://original.jpg",
            tags = "원래태그",
            user = user
        )

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board))

        val result = boardService.updateBoard(userId, boardId, request)

        assertThat(result.title).isEqualTo("수정된 제목")
        assertThat(result.content).isEqualTo("수정된 내용")
        assertThat(result.weather).isEqualTo(WeatherType.CLOUDY)
    }

    @Test
    fun `작성자가 아닌 사용자가 수정 시도 시 예외 발생`() {
        val ownerId = 1L
        val otherUserId = 2L
        val boardId = 1L
        val request = BoardUpdateRequest(
            title = "수정 시도",
            content = "수정 내용",
            weather = WeatherType.SUNNY
        )

        val owner = User(ownerId, "owner@test.com", "test123!", "작성자")
        val board = Board(boardId, "제목", "내용", WeatherType.SUNNY, null, null, owner)

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board))

        assertThatThrownBy {
            boardService.updateBoard(otherUserId, boardId, request)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `게시글 삭제 성공`() {
        val userId = 1L
        val boardId = 1L

        val user = User(userId, "test@test.com", "test123!", "테스터")
        val board = Board(boardId, "제목", "내용", WeatherType.SUNNY, null, null, user)

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board))

        boardService.deleteBoard(userId, boardId)

        verify(boardRepository, times(1)).delete(board)
    }

    @Test
    fun `작성자가 아닌 사용자가 삭제 시도 시 예외 발생`() {
        val ownerId = 1L
        val otherUserId = 2L
        val boardId = 1L

        val owner = User(ownerId, "owner@test.com", "test123!", "작성자")
        val board = Board(boardId, "제목", "내용", WeatherType.SUNNY, null, null, owner)

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board))

        assertThatThrownBy {
            boardService.deleteBoard(otherUserId, boardId)
        }.isInstanceOf(IllegalArgumentException::class.java)

        verify(boardRepository, never()).delete(any())
    }
}