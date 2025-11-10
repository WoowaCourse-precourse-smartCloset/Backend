package precourse.smartcloset.user.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Autowired
    private val sessionAuthInterceptor: SessionAuthInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(sessionAuthInterceptor)
            .addPathPatterns(
                "/api/v1/boards/**",
                "/api/v1/comments/**",
                "/api/v1/users/me",
                "/api/v1/users/withdraw"
            )
            .excludePathPatterns(
                "/api/v1/users/register",
                "/api/v1/users/login"
            )
    }
}