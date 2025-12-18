package com.b1nd.dauthserver.infrastructure.security.filter

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.Base64

@Component
class SwaggerBasicAuthFilter(
    @Value("\${swagger.username}") private val swaggerUsername: String,
    @Value("\${swagger.password}") private val swaggerPassword: String
) : WebFilter {

    private val swaggerPaths = listOf(
        "/swagger-ui.html",
        "/swagger-ui/",
        "/v3/api-docs",
        "/webjars/"
    )

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val path = exchange.request.uri.path

        // Swagger 경로가 아니면 통과
        if (!isSwaggerPath(path)) {
            return chain.filter(exchange)
        }

        // Authorization 헤더 확인
        val authHeader = exchange.request.headers.getFirst("Authorization")

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return unauthorized(exchange)
        }

        try {
            // Basic Auth 크레덴셜 파싱
            val base64Credentials = authHeader.substring(6)
            val credentials = String(Base64.getDecoder().decode(base64Credentials))
            val values = credentials.split(":", limit = 2)

            if (values.size != 2) {
                return unauthorized(exchange)
            }

            val username = values[0]
            val password = values[1]

            // 인증 확인
            if (username == swaggerUsername && password == swaggerPassword) {
                return chain.filter(exchange)
            }

            return unauthorized(exchange)
        } catch (e: Exception) {
            return unauthorized(exchange)
        }
    }

    private fun isSwaggerPath(path: String): Boolean {
        return swaggerPaths.any { path.startsWith(it) }
    }

    private fun unauthorized(exchange: ServerWebExchange): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        response.headers.add("WWW-Authenticate", "Basic realm=\"Swagger Documentation\"")
        return response.setComplete()
    }
}
