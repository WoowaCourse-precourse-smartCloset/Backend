package precourse.smartcloset.Recommendation.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>,
    @JsonProperty("max_tokens") val maxTokens: Int,
    val temperature: Double
)