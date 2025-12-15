package com.b1nd.dauthserver.application.token.data

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토큰 재발급 응답")
data class TokenRefreshResponse(
    @Schema(description = "새로 발급된 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val access: String
)
