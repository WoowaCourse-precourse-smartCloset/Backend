package precourse.smartcloset.Recommendation.util

import org.springframework.stereotype.Component
import precourse.smartcloset.Recommendation.dto.RecommendationRequest
import precourse.smartcloset.common.util.Constants.INVALID_TEMPERATURE_ERROR_MESSAGE

@Component
class RecommendationValidator {

    fun validate(request: RecommendationRequest) {
        validateTemperature(request.temperature)
    }

    private fun validateTemperature(temperature: Int) {
        require(temperature in -50..50) { INVALID_TEMPERATURE_ERROR_MESSAGE }
    }
}
