package precourse.smartcloset.Board.service

import precourse.smartcloset.Board.dto.BoardListResponse
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardResponse
import precourse.smartcloset.Board.dto.BoardUpdateRequest

interface BoardService {
    fun createBoard(userId: Long, request: BoardRequest): BoardResponse
    fun getBoardList(lastId: Long?, size: Int): BoardListResponse
    fun getBoardById(boardId: Long): BoardResponse
    fun updateBoard(userId: Long, boardId: Long, request: BoardUpdateRequest): BoardResponse
    fun deleteBoard(userId: Long, boardId: Long)
}