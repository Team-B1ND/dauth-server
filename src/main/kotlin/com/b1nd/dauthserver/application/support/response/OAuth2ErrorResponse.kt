package com.b1nd.dauthserver.application.support.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OAuth2ErrorResponse(
    val error: String,
    @JsonProperty("error_description")
    val errorDescription: String? = null,
    @JsonProperty("error_uri")
    val errorUri: String? = null
) {
    companion object {
        fun invalidRequest(description: String? = null) = OAuth2ErrorResponse(
            error = "invalid_request",
            errorDescription = description
        )

        fun invalidClient(description: String? = null) = OAuth2ErrorResponse(
            error = "invalid_client",
            errorDescription = description
        )

        fun invalidGrant(description: String? = null) = OAuth2ErrorResponse(
            error = "invalid_grant",
            errorDescription = description
        )

        fun unauthorizedClient(description: String? = null) = OAuth2ErrorResponse(
            error = "unauthorized_client",
            errorDescription = description
        )

        fun unsupportedGrantType(description: String? = null) = OAuth2ErrorResponse(
            error = "unsupported_grant_type",
            errorDescription = description
        )

        fun invalidScope(description: String? = null) = OAuth2ErrorResponse(
            error = "invalid_scope",
            errorDescription = description
        )

        fun serverError(description: String? = null) = OAuth2ErrorResponse(
            error = "server_error",
            errorDescription = description
        )
    }
}
