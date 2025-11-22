package precourse.smartcloset.common.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import precourse.smartcloset.user.config.SessionAuthInterceptor

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

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",                                              // React 개발 서버
                "http://ec2-13-124-254-196.ap-northeast-2.compute.amazonaws.com"    // 프로덕션
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
            .allowedHeaders("*")
            .allowCredentials(true)  // 쿠키/세션 허용
            .maxAge(3600)           // preflight 캐시 1시간
    }
}