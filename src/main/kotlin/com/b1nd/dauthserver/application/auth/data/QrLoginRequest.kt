package com.b1nd.dauthserver.application.auth.data

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "QR 로그인 요청 (앱에서 호출)")
data class QrLoginRequest(
    @field:NotBlank
    @Schema(description = "QR 코드에서 읽은 세션 코드", example = "550e8400-e29b-41d4-a716-446655440000")
    val code: String,

    @field:NotBlank
    @Schema(description = "앱의 도담도담 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val access: String,

    @field:NotBlank
    @Schema(description = "앱의 도담도담 Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refresh: String,

    @field:NotBlank
    @Schema(description = "OAuth 클라이언트 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: String
)