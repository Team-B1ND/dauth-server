package com.b1nd.dauthserver.application.auth.data

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 응답")
data class LoginResponse(
    @Schema(description = "Authorization Code (토큰 발급에 사용)", example = "abc123-def456-ghi789")
    val code: String,

    @Schema(description = "리다이렉트 URL", example = "https://myapp.com/callback")
    val redirectUrl: String
) {
    companion object {
        fun of(code: String, redirectUrl: String) =
            LoginResponse(code, redirectUrl)
    }
}