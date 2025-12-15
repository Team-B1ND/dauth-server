package com.b1nd.dauthserver.application.token.data

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "토큰 재발급 요청")
data class TokenRefreshRequest(
    @field:NotBlank
    @Schema(description = "기존에 발급받은 Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refresh: String,

    @field:NotBlank
    @Schema(description = "애플리케이션의 Client ID", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: String
)