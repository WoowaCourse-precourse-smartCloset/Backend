package precourse.smartcloset.Board.dto

import precourse.smartcloset.Board.entity.WeatherType

data class BoardRequest(
    val title: String,
    val content: String,
    val weather: WeatherType,
    val tags: String?,
)