package com.b1nd.dauthserver.application.auth.data

import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

@Schema(description = "QR 로그인 세션 생성 요청")
data class CreateQrRequest(
    @field:NotEmpty
    @Schema(description = "클라이언트 ID", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    val clientId: String,
    @field:NotEmpty
    @Schema(description = "요청할 scope 목록", example = "[\"OPENID\", \"READ_PROFILE\"]")
    val scopes: List<ScopeType>
)
