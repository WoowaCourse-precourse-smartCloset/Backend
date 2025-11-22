package precourse.smartcloset.Recommendation.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import precourse.smartcloset.Board.entity.WeatherType
import precourse.smartcloset.Recommendation.dto.RecommendationRequest
import precourse.smartcloset.Recommendation.dto.RecommendationResponse
import precourse.smartcloset.Recommendation.util.RecommendationValidator

@Service
class ClothingRecommendationService(
    private val openAIService: OpenAIService,
    private val validator: RecommendationValidator
) {

    @Transactional(readOnly = true)
    @Cacheable(
        value = ["clothingRecommendations"],
        keyGenerator = "recommendationKeyGenerator",
        unless = "#result == null"
    )
    fun getRecommendation(request: RecommendationRequest): RecommendationResponse {

        validator.validate(request)

        val prompt = createPrompt(request)
        val gptResponse = generateRecommendation(prompt)

        return createRecommendationResponse(request, gptResponse)
    }

    private fun createPrompt(request: RecommendationRequest): String {
        val basePrompt = createBasePrompt(request)
        val additionalInfo = createAdditionalInfo(request)

        return combinePromptParts(basePrompt, additionalInfo)
    }

    private fun createBasePrompt(request: RecommendationRequest): String {
        return buildString {
            append("날씨: ${getWeatherDescription(request.weather)}\n")
            append("기온: ${request.temperature}도\n")
        }
    }

    private fun createAdditionalInfo(request: RecommendationRequest): String {
        return buildString {
            request.occasion?.let { append("상황: $it\n") }
            request.gender?.let { append("성별: $it\n") }
            request.style?.let { append("선호 스타일: $it\n") }
        }
    }

    private fun combinePromptParts(basePrompt: String, additionalInfo: String): String {
        return "$basePrompt$additionalInfo\n위 조건에 맞는 옷차림을 추천해주세요."
    }

    private fun generateRecommendation(prompt: String): String {
        return openAIService.generateClothingRecommendation(prompt)
    }

    private fun getWeatherDescription(weather: WeatherType): String {
        return when (weather) {
            WeatherType.SUNNY -> "맑음"
            WeatherType.CLOUDY -> "흐림"
            WeatherType.RAINY -> "비"
            WeatherType.SNOWY -> "눈"
            WeatherType.WINDY -> "바람 많음"
        }
    }

    private fun createRecommendationResponse(
        request: RecommendationRequest,
        gptResponse: String
    ): RecommendationResponse {
        return RecommendationResponse(
            recommendation = gptResponse,
            weather = getWeatherDescription(request.weather),
            temperature = request.temperature,
            suggestedItems = extractSuggestedItems(gptResponse)
        )
    }

    private fun extractSuggestedItems(response: String): List<String> {
        val keywords = getSuggestedItemKeywords()
        return findMatchingKeywords(response, keywords)
    }

    private fun getSuggestedItemKeywords(): List<String> {
        return listOf(
            "상의", "하의", "아우터", "외투", "코트", "재킷",
            "셔츠", "티셔츠", "맨투맨", "후드", "니트",
            "청바지", "슬랙스", "면바지", "치마", "원피스",
            "스니커즈", "구두", "부츠", "샌들",
            "모자", "가방", "스카프", "목도리", "선글라스"
        )
    }

    private fun findMatchingKeywords(response: String, keywords: List<String>): List<String> {
        return keywords
            .filter { keyword -> response.contains(keyword) }
            .distinct()
            .take(10)
    }
}