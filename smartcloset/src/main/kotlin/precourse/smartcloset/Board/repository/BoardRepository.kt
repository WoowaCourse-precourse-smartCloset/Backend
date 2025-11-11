package precourse.smartcloset.Board.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import precourse.smartcloset.Board.entity.Board

@Repository
interface BoardRepository : JpaRepository<Board, Long>