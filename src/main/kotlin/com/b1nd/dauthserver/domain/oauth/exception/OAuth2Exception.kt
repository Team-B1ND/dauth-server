package com.b1nd.dauthserver.domain.oauth.exception

import org.springframework.http.HttpStatus

class OAuth2Exception(
    val error: String,
    val errorDescription: String? = null,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : RuntimeException(errorDescription ?: error) {

    companion object {
        fun invalidRequest(description: String? = null) = OAuth2Exception(
            error = "invalid_request",
            errorDescription = description,
            httpStatus = HttpStatus.BAD_REQUEST
        )

        fun invalidClient(description: String? = null) = OAuth2Exception(
            error = "invalid_client",
            errorDescription = description,
            httpStatus = HttpStatus.UNAUTHORIZED
        )

        fun invalidGrant(description: String? = null) = OAuth2Exception(
            error = "invalid_grant",
            errorDescription = description,
            httpStatus = HttpStatus.BAD_REQUEST
        )

        fun unauthorizedClient(description: String? = null) = OAuth2Exception(
            error = "unauthorized_client",
            errorDescription = description,
            httpStatus = HttpStatus.UNAUTHORIZED
        )

        fun unsupportedGrantType(description: String? = null) = OAuth2Exception(
            error = "unsupported_grant_type",
            errorDescription = description,
            httpStatus = HttpStatus.BAD_REQUEST
        )

        fun serverError(description: String? = null) = OAuth2Exception(
            error = "server_error",
            errorDescription = description,
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}
