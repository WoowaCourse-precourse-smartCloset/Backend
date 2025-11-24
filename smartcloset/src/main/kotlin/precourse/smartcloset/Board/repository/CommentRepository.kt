package precourse.smartcloset.Board.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import precourse.smartcloset.Board.entity.Comment

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.board.id = :boardId ORDER BY c.createdAt ASC")
    fun findByBoardIdWithUser(@Param("boardId") boardId: Long): List<Comment>
}