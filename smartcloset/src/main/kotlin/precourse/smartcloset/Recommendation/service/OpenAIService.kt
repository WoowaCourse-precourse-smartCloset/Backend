package precourse.smartcloset.Recommendation.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import precourse.smartcloset.Recommendation.dto.ChatCompletionRequest
import precourse.smartcloset.Recommendation.dto.ChatCompletionResponse
import precourse.smartcloset.Recommendation.dto.Message
import precourse.smartcloset.common.util.Constants.OPENAI_API_CALL_FAILED_ERROR_MESSAGE

@Service
class OpenAIService(
    @Value("\${openai.api.key}") private val apiKey: String,
    @Value("\${openai.api.model}") private val model: String,
    @Value("\${openai.api.max-tokens}") private val maxTokens: Int,
    @Value("\${openai.api.temperature}") private val temperature: Double
) {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    fun generateClothingRecommendation(prompt: String): String {
        val request = createChatCompletionRequest(prompt)

        return callOpenAIAPI(request)
    }

    private fun createChatCompletionRequest(prompt: String): ChatCompletionRequest {
        return ChatCompletionRequest(
            model = model,
            messages = listOf(
                Message(
                    role = "system",
                    content = """
                        당신은 패션 전문가입니다. 
                        날씨와 상황에 맞는 옷차림을 추천해주세요.
                        추천은 다음 형식으로 제공해주세요:
                        1. 전체적인 코디 설명
                        2. 상의 추천
                        3. 하의 추천
                        4. 아우터 추천 (필요시)
                        5. 액세서리 추천 (필요시)
                        6. 주의사항
                    """.trimIndent()
                ),
                Message(role = "user", content = prompt)
            ),
            maxTokens = maxTokens,
            temperature = temperature
        )
    }

    private fun callOpenAIAPI(request: ChatCompletionRequest): String {
        return runCatching {
            val response = webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatCompletionResponse::class.java)
                .block()

            extractContentFromResponse(response)
        }.getOrElse {
            throw IllegalStateException(OPENAI_API_CALL_FAILED_ERROR_MESSAGE)
        }
    }

    private fun extractContentFromResponse(response: ChatCompletionResponse?): String {
        return response?.choices?.firstOrNull()?.message?.content
            ?: throw IllegalStateException(OPENAI_API_CALL_FAILED_ERROR_MESSAGE)
    }
}