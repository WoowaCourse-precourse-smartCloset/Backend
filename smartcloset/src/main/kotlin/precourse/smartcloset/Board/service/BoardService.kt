package precourse.smartcloset.Board.service

import precourse.smartcloset.Board.dto.BoardListResponse
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardResponse

interface BoardService {
    fun createBoard(userId: Long, request: BoardRequest): BoardResponse
    fun getBoardList(lastId: Long?, size: Int): BoardListResponse
    fun getBoardById(boardId: Long): BoardResponse
}