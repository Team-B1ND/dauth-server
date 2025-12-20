package com.b1nd.dauthserver.infrastructure.security.filter

import com.b1nd.dauthserver.application.support.response.ErrorResponse
import com.b1nd.dauthserver.domain.common.exception.BasicException
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class FilterExceptionHandler(
    private val objectMapper: ObjectMapper
): WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        return chain.filter(exchange)
            .onErrorResume(BasicException::class.java) { e ->
                val response = exchange.response
                response.statusCode = HttpStatus.valueOf(e.statusCode.status)
                response.headers.contentType = MediaType.APPLICATION_JSON

                val body = ErrorResponse(e.statusCode.status, e.statusCode.message)
                val bytes = objectMapper.writeValueAsBytes(body)

                val buffer = response.bufferFactory().wrap(bytes)
                response.writeWith(Mono.just(buffer))
            }
            .onErrorResume(ExpiredJwtException::class.java) { _ ->
                writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다")
            }
            .onErrorResume(JwtException::class.java) { _ ->
                writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다")
            }
    }

    private fun writeErrorResponse(
        exchange: ServerWebExchange,
        status: HttpStatus,
        message: String
    ): Mono<Void> {
        val response = exchange.response
        response.statusCode = status
        response.headers.contentType = MediaType.APPLICATION_JSON

        val body = ErrorResponse(status.value(), message)
        val bytes = objectMapper.writeValueAsBytes(body)

        val buffer = response.bufferFactory().wrap(bytes)
        return response.writeWith(Mono.just(buffer))
    }
}