package com.b1nd.dauthserver.presentation.oauth

import com.b1nd.dauthserver.application.oauth.OAuthUseCase
import com.b1nd.dauthserver.application.oauth.data.StandardUserInfoResponse
import com.b1nd.dauthserver.application.token.TokenUseCase
import com.b1nd.dauthserver.application.token.data.InternalTokenRequest
import com.b1nd.dauthserver.application.token.data.StandardTokenResponse
import com.b1nd.dauthserver.domain.app.exception.ApplicationKeyNotMatchException
import com.b1nd.dauthserver.domain.app.exception.ApplicationNotFoundException
import com.b1nd.dauthserver.domain.oauth.exception.OAuth2Exception
import com.b1nd.dauthserver.domain.user.exception.UserNotFoundException
import com.b1nd.dauthserver.infrastructure.database.redis.exception.RedisKeyNotFoundException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Base64

@Tag(name = "OAuth Token", description = "OAuth 2.0 토큰 엔드포인트")
@RestController
class TokenController(
    private val tokenUseCase: TokenUseCase,
    private val oAuthUseCase: OAuthUseCase
) {
    @Operation(
        summary = "토큰 발급",
        description = """
            OAuth 2.0 토큰 엔드포인트 (RFC 6749)

            **지원하는 grant_type:**
            - authorization_code: Authorization Code로 토큰 발급
            - refresh_token: Refresh Token으로 Access Token 재발급

            **인증 방식:**
            - client_secret_post: client_id, client_secret을 body에 포함
            - client_secret_basic: Authorization 헤더에 Basic 인증
        """
    )
    @PostMapping(
        "/oauth/token",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun token(
        @RequestParam("grant_type") grantType: String,
        @RequestParam(required = false) code: String?,
        @RequestParam("redirect_uri", required = false) redirectUri: String?,
        @RequestParam("client_id", required = false) clientId: String?,
        @RequestParam("client_secret", required = false) clientSecret: String?,
        @RequestParam("refresh_token", required = false) refreshToken: String?,
        @RequestParam("code_verifier", required = false) codeVerifier: String?,
        @RequestHeader("Authorization", required = false) authorization: String?
    ): StandardTokenResponse {
        try {
            val (resolvedClientId, resolvedClientSecret) = resolveCredentials(
                clientId, clientSecret, authorization
            )

            return when (grantType) {
                "authorization_code" -> {
                    if (code.isNullOrBlank()) {
                        throw OAuth2Exception.invalidRequest("code is required for authorization_code grant")
                    }
                    tokenUseCase.issueToken(code, resolvedClientId, resolvedClientSecret)
                }
                "refresh_token" -> {
                    if (refreshToken.isNullOrBlank()) {
                        throw OAuth2Exception.invalidRequest("refresh_token is required for refresh_token grant")
                    }
                    tokenUseCase.refreshToken(refreshToken, resolvedClientId, resolvedClientSecret)
                }
                else -> throw OAuth2Exception.unsupportedGrantType("Unsupported grant_type: $grantType")
            }
        } catch (e: OAuth2Exception) {
            throw e
        } catch (e: ApplicationNotFoundException) {
            throw OAuth2Exception.invalidClient("Invalid client_id")
        } catch (e: ApplicationKeyNotMatchException) {
            throw OAuth2Exception.invalidClient("Invalid client credentials")
        } catch (e: UserNotFoundException) {
            throw OAuth2Exception.invalidGrant("User not found")
        } catch (e: RedisKeyNotFoundException) {
            throw OAuth2Exception.invalidGrant("Invalid or expired authorization code")
        } catch (e: Exception) {
            throw OAuth2Exception.serverError(e.message)
        }
    }

    private fun resolveCredentials(
        clientId: String?,
        clientSecret: String?,
        authorization: String?
    ): Pair<String, String> {
        if (!clientId.isNullOrBlank() && !clientSecret.isNullOrBlank()) {
            return clientId to clientSecret
        }

        if (!authorization.isNullOrBlank() && authorization.startsWith("Basic ")) {
            val decoded = String(Base64.getDecoder().decode(authorization.substring(6)))
            val parts = decoded.split(":", limit = 2)
            if (parts.size == 2) {
                return parts[0] to parts[1]
            }
        }

        throw OAuth2Exception.invalidClient("Client credentials are required")
    }

    @Operation(
        summary = "사용자 정보 조회",
        description = "OAuth 2.0 UserInfo 엔드포인트 (OpenID Connect)",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @GetMapping("/userinfo", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getUserInfo(): StandardUserInfoResponse =
        oAuthUseCase.getStandardUserInfo()

    @Operation(
        summary = "내부 토큰 발급 (DAuth 전용)",
        description = """
            DAuth 내부 시스템 전용 토큰 발급 엔드포인트입니다.
            client_secret 없이 client_id만으로 토큰을 발급합니다.

            **용도:** DAuth 관리 페이지 등 내부 시스템에서 사용

            **지원하는 grant_type:**
            - authorization_code: Authorization Code로 토큰 발급
            - refresh_token: Refresh Token으로 Access Token 재발급
        """
    )
    @PostMapping("/oauth/token/internal")
    suspend fun tokenInternal(
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
