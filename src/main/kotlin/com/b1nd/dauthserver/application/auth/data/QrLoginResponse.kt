package com.b1nd.dauthserver.application.auth.data

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "QR 로그인 세션 생성 응답")
data class QrLoginResponse(
    @Schema(description = "QR 세션 코드 (QR 코드에 인코딩)", example = "550e8400-e29b-41d4-a716-446655440000")
    val code: String
)