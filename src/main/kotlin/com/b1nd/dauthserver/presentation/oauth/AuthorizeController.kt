package com.b1nd.dauthserver.presentation.oauth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Tag(name = "OAuth Authorization", description = "OAuth 2.0 Authorization 엔드포인트")
@RestController
class AuthorizeController(
    @Value("\${app.oauth.login-page:https://dauth.b1nd.com/login}") private val loginPage: String
) {
    @Operation(
        summary = "Authorization 요청",
        description = """
            OAuth 2.0 Authorization Code Flow의 시작점입니다.
            사용자를 로그인 페이지로 리다이렉트합니다.

            Spring Security OAuth2 Client가 자동으로 호출합니다.
        """
    )
    @GetMapping("/oauth/authorize")
    suspend fun authorize(
        @Parameter(description = "클라이언트 ID") @RequestParam("client_id") clientId: String,
        @Parameter(description = "리다이렉트 URI") @RequestParam("redirect_uri") redirectUri: String,
        @Parameter(description = "응답 타입 (code)") @RequestParam("response_type") responseType: String,
        @Parameter(description = "요청 스코프") @RequestParam(required = false) scope: String?,
        @Parameter(description = "상태값 (CSRF 방지)") @RequestParam(required = false) state: String?,
        @Parameter(description = "PKCE code_challenge (호환성용, 검증하지 않음)") @RequestParam("code_challenge", required = false) codeChallenge: String?,
        @Parameter(description = "PKCE code_challenge_method (호환성용, 검증하지 않음)") @RequestParam("code_challenge_method", required = false) codeChallengeMethod: String?,
        response: ServerHttpResponse
    ) {
        val encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
        val encodedScope = scope?.let { URLEncoder.encode(it, StandardCharsets.UTF_8) } ?: "openid"
        val encodedState = state?.let { URLEncoder.encode(it, StandardCharsets.UTF_8) }

        val loginUrl = buildString {
            append(loginPage)
            append("?client_id=$clientId")
            append("&redirect_uri=$encodedRedirectUri")
            append("&scope=$encodedScope")
            encodedState?.let { append("&state=$it") }
        }

        response.statusCode = HttpStatus.FOUND
        response.headers.location = URI.create(loginUrl)
    }
}
