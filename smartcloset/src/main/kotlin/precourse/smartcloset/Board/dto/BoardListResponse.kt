package precourse.smartcloset.Board.dto

data class BoardListResponse(
    val boards: List<BoardResponse>,
    val hasNext: Boolean,
    val nextLastId: Long?
)