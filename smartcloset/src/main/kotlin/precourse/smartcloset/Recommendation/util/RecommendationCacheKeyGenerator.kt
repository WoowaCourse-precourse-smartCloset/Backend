package precourse.smartcloset.Recommendation.util

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.stereotype.Component
import precourse.smartcloset.Recommendation.dto.RecommendationRequest
import java.lang.reflect.Method

@Component("recommendationKeyGenerator")
class RecommendationCacheKeyGenerator : KeyGenerator {

    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        val request = params.firstOrNull() as? RecommendationRequest
            ?: return "default"

        return buildCacheKey(request)
    }

    private fun buildCacheKey(request: RecommendationRequest): String {
        return buildString {
            append("weather:${request.weather}")
            append(":temp:${request.temperature}")
            append(":occasion:${request.occasion ?: "none"}")
            append(":gender:${request.gender ?: "none"}")
            append(":style:${request.style ?: "none"}")
        }
    }
}