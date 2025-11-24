package precourse.smartcloset.Recommendation.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import precourse.smartcloset.Board.entity.WeatherType
import precourse.smartcloset.Recommendation.dto.RecommendationRequest
import precourse.smartcloset.Recommendation.util.RecommendationValidator

@ExtendWith(MockitoExtension::class)
class ClothingRecommendationServiceTest {

    @Mock
    lateinit var openAIService: OpenAIService

    @Mock
    lateinit var validator: RecommendationValidator

    @InjectMocks
    lateinit var clothingRecommendationService: ClothingRecommendationService

    @Test
    fun `추천 요청시 validator와 OpenAI를 호출하고 응답을 매핑한다`() {
        // given
        val request = RecommendationRequest(
            weather = WeatherType.SUNNY,
            temperature = 23,
            occasion = "출근",
            gender = "여성",
            style = "캐주얼"
        )

        val gptResponse = """
            1. 전체적인 코디 설명: 가벼운 캐주얼 출근룩
            2. 상의 추천: 얇은 니트나 티셔츠
            3. 하의 추천: 슬림한 청바지
            4. 아우터 추천: 간단한 재킷
            5. 액세서리 추천: 모자, 가벼운 스카프
            6. 주의사항: 실내 에어컨을 고려해 얇은 아우터를 준비하세요.
        """.trimIndent()

        given(openAIService.generateClothingRecommendation(anyString()))
            .willReturn(gptResponse)

        // when
        val result = clothingRecommendationService.getRecommendation(request)

        // then
        assertThat(result.weather).isEqualTo("맑음")
        assertThat(result.temperature).isEqualTo(23)
        assertThat(result.recommendation).isEqualTo(gptResponse)
    }
}
