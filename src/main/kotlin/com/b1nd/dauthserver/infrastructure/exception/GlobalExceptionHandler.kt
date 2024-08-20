package com.b1nd.dauthserver.infrastructure.exception

import com.b1nd.dauthserver.application.common.ErrorResponse
import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.exception.InternalServerException
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Component
@Order(-2)
class CustomGlobalExceptionHandler(
    errorAttributes: ErrorAttributes,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, WebProperties.Resources(), applicationContext) {

    init {
        super.setMessageWriters(serverCodecConfigurer.writers)
        super.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), this::handleException)
    }

    private fun handleException(request: ServerRequest): Mono<ServerResponse> {
        return when (val ex = super.getError(request)) {
            is BasicException -> buildErrorResponse(ex)
            is ErrorResponseException -> buildErrorResponse(BasicException(ex.message,ex.statusCode.value()))
            else -> buildErrorResponse(InternalServerException)
        }
    }

    private fun buildErrorResponse(e: BasicException): Mono<ServerResponse> {
        return ServerResponse.status(e.statusCode).bodyValue(ErrorResponse(e.statusCode, e.message))
    }

}