package precourse.smartcloset.Board.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import precourse.smartcloset.Board.dto.CommentRequest
import precourse.smartcloset.Board.dto.CommentResponse
import precourse.smartcloset.Board.entity.Board
import precourse.smartcloset.Board.entity.Comment
import precourse.smartcloset.Board.entity.WeatherType
import precourse.smartcloset.Board.repository.BoardRepository
import precourse.smartcloset.Board.repository.CommentRepository
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository
import java.util.*

@ExtendWith(MockitoExtension::class)
class CommentServiceImplTest {

    @Mock lateinit var commentRepository: CommentRepository
    @Mock lateinit var boardRepository: BoardRepository
    @Mock lateinit var userRepository: UserRepository
    @Mock lateinit var validator: Validator

    @InjectMocks
    lateinit var commentService: CommentServiceImpl

    @Test
    fun `댓글 생성 성공`() {
        // given
        val user = User(id = 1L, email = "a@a.com", password = "pw", nickname = "테스터")
        val board = Board(
            id = 2L,
            title = "제목",
            content = "내용",
            weather = WeatherType.SUNNY,
            user = user
        )
        val request = CommentRequest(content = "댓글 내용")
        val savedComment = Comment(id = 3L, content = "댓글 내용", board = board, user = user)

        given(userRepository.findById(1L)).willReturn(Optional.of(user))
        given(boardRepository.findById(2L)).willReturn(Optional.of(board))
        given(commentRepository.save(any(Comment::class.java))).willReturn(savedComment)

        // when
        val result: CommentResponse = commentService.createComment(1L, 2L, request)

        // then
        verify(validator).validateCommentContent("댓글 내용")
        verify(commentRepository).save(any(Comment::class.java))
        assertThat(result.commentId).isEqualTo(3L)
        assertThat(result.content).isEqualTo("댓글 내용")
        assertThat(result.nickname).isEqualTo("테스터")
    }

    @Test
    fun `댓글 수정 실패 - 작성자 아님`() {
        // given
        val writer = User(id = 1L, email = "a@a.com", password = "pw", nickname = "작성자")
        val otherUserId = 99L
        val board = Board(
            id = 2L,
            title = "제목",
            content = "내용",
            weather = WeatherType.CLOUDY,
            user = writer
        )
        val comment = Comment(id = 3L, content = "원본", board = board, user = writer)
        val request = CommentRequest("수정된 내용")

        given(commentRepository.findById(3L)).willReturn(Optional.of(comment))

        // when & then
        val ex = assertThrows<IllegalArgumentException> {
            commentService.updateComment(otherUserId, 3L, request)
        }

        assertThat(ex.message).contains("권한")
    }

    @Test
    fun `댓글 삭제 성공`() {
        // given
        val user = User(id = 1L, email = "a@a.com", password = "pw", nickname = "작성자")
        val board = Board(
            id = 2L,
            title = "제목",
            content = "내용",
            weather = WeatherType.SUNNY,
            user = user
        )
        val comment = Comment(id = 3L, content = "삭제대상", board = board, user = user)

        given(commentRepository.findById(3L)).willReturn(Optional.of(comment))

        // when
        commentService.deleteComment(1L, 3L)

        // then
        verify(commentRepository).delete(comment)
    }

    @Test
    fun `게시글의 모든 댓글 조회`() {
        // given
        val user = User(id = 1L, email = "a@a.com", password = "pw", nickname = "닉")
        val board = Board(
            id = 2L,
            title = "제목",
            content = "내용",
            weather = WeatherType.RAINY,
            user = user
        )
        val comments = listOf(
            Comment(id = 10L, content = "첫 댓글", board = board, user = user),
            Comment(id = 11L, content = "두 번째", board = board, user = user)
        )

        given(boardRepository.existsById(2L)).willReturn(true)
        given(commentRepository.findByBoardIdWithUser(2L)).willReturn(comments)

        // when
        val result = commentService.getCommentsByBoardId(2L)

        // then
        verify(boardRepository).existsById(2L)
        assertThat(result).hasSize(2)
        assertThat(result.first().content).isEqualTo("첫 댓글")
    }
}
