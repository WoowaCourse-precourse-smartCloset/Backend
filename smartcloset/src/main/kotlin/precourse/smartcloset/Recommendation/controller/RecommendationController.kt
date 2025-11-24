package precourse.smartcloset.Recommendation.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import precourse.smartcloset.Recommendation.dto.RecommendationRequest
import precourse.smartcloset.Recommendation.dto.RecommendationResponse
import precourse.smartcloset.Recommendation.service.ClothingRecommendationService
import precourse.smartcloset.common.dto.ApiResponse
import precourse.smartcloset.common.util.Constants.RECOMMENDATION_SUCCESS_MESSAGE

@CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/recommendations")
class RecommendationController(
    private val clothingRecommendationService: ClothingRecommendationService
) {

    @PostMapping
    fun getClothingRecommendation(
        @RequestBody request: RecommendationRequest
    ): ResponseEntity<ApiResponse<RecommendationResponse>> {
        val response = clothingRecommendationService.getRecommendation(request)
        return createSuccessResponse(response)
    }

    private fun createSuccessResponse(
        data: RecommendationResponse
    ): ResponseEntity<ApiResponse<RecommendationResponse>> {
        val apiResponse = ApiResponse.success(
            message = RECOMMENDATION_SUCCESS_MESSAGE,
            data = data
        )
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }
}