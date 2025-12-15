package com.b1nd.dauthserver.infrastructure.security.configuration

import com.b1nd.dauthserver.infrastructure.security.filter.FilterExceptionHandler
import com.b1nd.dauthserver.infrastructure.security.filter.TokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val tokenFilter: TokenFilter,
    private val filterExceptionHandler: FilterExceptionHandler
) {
    @Bean
    protected fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .csrf { it.disable() }
            .cors { corsSpec -> corsSpec.configurationSource(corsConfigurationSource()) }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { exchange, _ ->
                    val response = exchange.response
                    response.statusCode = org.springframework.http.HttpStatus.UNAUTHORIZED
                    response.headers.contentType = org.springframework.http.MediaType.APPLICATION_JSON
                    val body = """{"status":401,"message":"Unauthorized"}"""
                    val buffer = response.bufferFactory().wrap(body.toByteArray())
                    response.writeWith(reactor.core.publisher.Mono.just(buffer))
                }
            }
            .authorizeExchange { it
                // Root
                .pathMatchers("/").permitAll()

                // Swagger UI
                .pathMatchers("/swagger-ui.html").permitAll()
                .pathMatchers("/swagger-ui/**").permitAll()
                .pathMatchers("/v3/api-docs/**").permitAll()
                .pathMatchers("/webjars/**").permitAll()

                // Authentication (로그인)
                .pathMatchers("/auth/**").permitAll()

                // OpenID Connect Discovery
                .pathMatchers("/.well-known/**").permitAll()

                // OAuth Authorization
                .pathMatchers(HttpMethod.GET, "/oauth/authorize").permitAll()

                // OAuth Token
                .pathMatchers(HttpMethod.POST, "/oauth/token").permitAll()
                .pathMatchers(HttpMethod.POST, "/oauth/token/**").permitAll()

                // OAuth JWKS (for JWT verification)
                .pathMatchers(HttpMethod.GET, "/oauth/jwks").permitAll()

                // Application (공개 목록 조회)
                .pathMatchers(HttpMethod.GET, "/app").permitAll()

                // Framework
                .pathMatchers(HttpMethod.GET, "/framework").permitAll()

                // 그 외 모든 요청은 인증 필요
                .anyExchange().authenticated()
            }
            .addFilterBefore(filterExceptionHandler, SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterAt(tokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()

    @Bean
    protected fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            addAllowedOriginPattern("*")
            addAllowedHeader("*")
            addAllowedMethod("*")
            allowCredentials = true
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}