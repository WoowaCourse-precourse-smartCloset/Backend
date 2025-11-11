package precourse.smartcloset.Board.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import precourse.smartcloset.Board.entity.Board

@Repository
interface BoardRepository : JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.id DESC")
    fun findAllWithUser(pageable: Pageable): List<Board>

    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id < :lastId ORDER BY b.id DESC")
    fun findByIdLessThanWithUser(@Param("lastId") lastId: Long, pageable: Pageable): List<Board>
}