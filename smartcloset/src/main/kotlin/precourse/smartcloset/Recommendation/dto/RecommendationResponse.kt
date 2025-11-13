package precourse.smartcloset.Recommendation.dto

data class RecommendationResponse(
    val recommendation: String,
    val weather: String,
    val temperature: Int,
    val suggestedItems: List<String>
)