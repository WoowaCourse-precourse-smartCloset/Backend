package precourse.smartcloset.Board.dto

import precourse.smartcloset.Board.entity.WeatherType

data class BoardRequest(
    val title: String,
    val content: String,
    val weather: WeatherType,
    val imageUrl: String? = null,
    val tags: List<String>? = null
)