package precourse.smartcloset.Board.service

import org.springframework.web.multipart.MultipartFile
import precourse.smartcloset.Board.dto.BoardListResponse
import precourse.smartcloset.Board.dto.BoardRequest
import precourse.smartcloset.Board.dto.BoardResponse
import precourse.smartcloset.Board.dto.BoardUpdateRequest

interface BoardService {
    fun createBoard(userId: Long, request: BoardRequest, imageFile: MultipartFile?): BoardResponse
    fun getBoardList(lastId: Long?, size: Int): BoardListResponse
    fun getBoardById(boardId: Long): BoardResponse
    fun updateBoard(userId: Long, boardId: Long, request: BoardUpdateRequest, imageFile: MultipartFile?): BoardResponse
    fun deleteBoard(userId: Long, boardId: Long)

    fun getMyBoards(userId: Long, lastId: Long?, size: Int): BoardListResponse
}