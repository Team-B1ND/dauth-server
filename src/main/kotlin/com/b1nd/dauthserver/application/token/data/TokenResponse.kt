package com.b1nd.dauthserver.application.token.data

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토큰 발급 응답")
data class TokenResponse(
    @Schema(description = "리소스 접근용 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,

    @Schema(description = "토큰 갱신용 Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refreshToken: String,

    @Schema(description = "사용자 정보가 담긴 ID Token (JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val idToken: String,

    @Schema(description = "토큰 타입", example = "Bearer")
    val tokenType: String = "Bearer"
)
