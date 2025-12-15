package com.b1nd.dauthserver.application.auth.data

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "QR 로그인 확인 요청 (웹에서 폴링)")
data class CheckQrRequest(
    @Schema(description = "QR 세션 코드", example = "550e8400-e29b-41d4-a716-446655440000")
    val code: String,

    @Schema(description = "로그인 후 리다이렉트될 URL", example = "https://myapp.com/callback")
    val redirectUrl: String
)
