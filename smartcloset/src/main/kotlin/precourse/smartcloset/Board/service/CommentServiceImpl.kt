package precourse.smartcloset.Board.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import precourse.smartcloset.Board.dto.CommentRequest
import precourse.smartcloset.Board.dto.CommentResponse
import precourse.smartcloset.Board.entity.Board
import precourse.smartcloset.Board.entity.Comment
import precourse.smartcloset.Board.repository.BoardRepository
import precourse.smartcloset.Board.repository.CommentRepository
import precourse.smartcloset.common.util.Constants.BOARD_NOT_FOUND_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.COMMENT_NOT_FOUND_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.COMMENT_UNAUTHORIZED_ERROR_MESSAGE
import precourse.smartcloset.common.util.Constants.USER_NOT_FOUND_ERROR_MESSAGE
import precourse.smartcloset.common.util.Validator
import precourse.smartcloset.user.entity.User
import precourse.smartcloset.user.repository.UserRepository

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository,
    private val validator: Validator
) : CommentService {

    @Transactional
    override fun createComment(userId: Long, boardId: Long, request: CommentRequest): CommentResponse {
        validator.validateCommentContent(request.content)

        val user = findUserById(userId)
        val board = findBoardById(boardId)
        val comment = createCommentEntity(request, board, user)
        val savedComment = commentRepository.save(comment)

        return CommentResponse.from(savedComment)
    }

    @Transactional(readOnly = true)
    override fun getCommentsByBoardId(boardId: Long): List<CommentResponse> {
        validateBoardExists(boardId)
        val comments = commentRepository.findByBoardIdWithUser(boardId)
        return convertToCommentResponses(comments)
    }

    @Transactional
    override fun updateComment(userId: Long, commentId: Long, request: CommentRequest): CommentResponse {
        validator.validateCommentContent(request.content)

        val comment = findCommentById(commentId)
        validateCommentOwner(comment, userId)
        updateCommentEntity(comment, request)

        return CommentResponse.from(comment)
    }

    @Transactional
    override fun deleteComment(userId: Long, commentId: Long) {
        val comment = findCommentById(commentId)
        validateCommentOwner(comment, userId)
        commentRepository.delete(comment)
    }

    private fun findUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException(USER_NOT_FOUND_ERROR_MESSAGE) }
    }

    private fun findBoardById(boardId: Long): Board {
        return boardRepository.findByIdOrNull(boardId)
            ?: throw IllegalArgumentException(BOARD_NOT_FOUND_ERROR_MESSAGE)
    }

    private fun findCommentById(commentId: Long): Comment {
        return commentRepository.findByIdOrNull(commentId)
            ?: throw IllegalArgumentException(COMMENT_NOT_FOUND_ERROR_MESSAGE)
    }

    private fun createCommentEntity(request: CommentRequest, board: Board, user: User): Comment {
        return Comment(
            content = request.content,
            board = board,
            user = user
        )
    }

    private fun validateBoardExists(boardId: Long) {
        if (!boardRepository.existsById(boardId)) {
            throw IllegalArgumentException(BOARD_NOT_FOUND_ERROR_MESSAGE)
        }
    }

    private fun validateCommentOwner(comment: Comment, userId: Long) {
        require(comment.user.id == userId) { COMMENT_UNAUTHORIZED_ERROR_MESSAGE }
    }

    private fun convertToCommentResponses(comments: List<Comment>): List<CommentResponse> {
        return comments.map { CommentResponse.from(it) }
    }

    private fun updateCommentEntity(comment: Comment, request: CommentRequest) {
        comment.update(request.content)
    }
}