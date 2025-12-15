package com.b1nd.dauthserver.application.token.data

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "토큰 발급 요청")
data class TokenRequest(
    @Schema(description = "로그인 후 받은 Authorization Code", example = "550e8400-e29b-41d4-a716-446655440000")
    val code: UUID,

    @Schema(description = "애플리케이션의 Client Secret", example = "secret-key-12345")
    val clientSecret: String
)