package com.b1nd.dauthserver.presentation.oauth

import com.b1nd.dauthserver.application.oauth.OAuthUseCase
import com.b1nd.dauthserver.application.oauth.data.StandardUserInfoResponse
import com.b1nd.dauthserver.application.token.TokenUseCase
import com.b1nd.dauthserver.application.token.data.InternalTokenRequest
import com.b1nd.dauthserver.application.token.data.StandardTokenResponse
import com.b1nd.dauthserver.application.token.data.TokenRequest
import com.b1nd.dauthserver.domain.oauth.exception.OAuth2Exception
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
class TokenController(
    private val tokenUseCase: TokenUseCase,
    private val oAuthUseCase: OAuthUseCase
) : TokenDocs {

    @PostMapping(
        "/oauth/token",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    override suspend fun token(
        exchange: ServerWebExchange,
        @RequestHeader("Authorization", required = false) authorization: String?
    ): StandardTokenResponse {
        return tokenUseCase.issue(extractRequest(exchange, authorization))
    }

    private suspend fun extractRequest(exchange: ServerWebExchange, authorization: String?): TokenRequest {
        val formData = exchange.formData.awaitSingle()
        return TokenRequest(
            grantType = formData.getFirst("grant_type")
                ?: throw OAuth2Exception.invalidRequest("grant_type is required"),
            code = formData.getFirst("code"),
            redirectUri = formData.getFirst("redirect_uri"),
            clientId = formData.getFirst("client_id"),
            clientSecret = formData.getFirst("client_secret"),
            refreshToken = formData.getFirst("refresh_token"),
            codeVerifier = formData.getFirst("code_verifier"),
            authorization = authorization
        )
    }

    @GetMapping("/userinfo", produces = [MediaType.APPLICATION_JSON_VALUE])
    override suspend fun getUserInfo(): StandardUserInfoResponse =
        oAuthUseCase.getStandardUserInfo()

    @PostMapping("/oauth/token/internal")
    override suspend fun tokenInternal(
        @RequestBody request: InternalTokenRequest
    ): StandardTokenResponse {
        return when (request.grantType) {
            "authorization_code" -> {
                requireNotNull(request.code) { "code is required for authorization_code grant" }
                tokenUseCase.issueTokenInternal(request.code, request.clientId)
            }
            "refresh_token" -> {
                requireNotNull(request.refreshToken) { "refresh_token is required for refresh_token grant" }
                tokenUseCase.refreshTokenInternal(request.refreshToken, request.clientId)
            }
            else -> throw IllegalArgumentException("Unsupported grant_type: ${request.grantType}")
        }
    }
}
