package precourse.smartcloset.Recommendation.dto

import precourse.smartcloset.Board.entity.WeatherType

data class RecommendationRequest(
    val weather: WeatherType,
    val temperature: Int,
    val occasion: String? = null,
    val gender: String? = null,
    val style: String? = null
)