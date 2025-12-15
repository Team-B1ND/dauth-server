package com.b1nd.dauthserver.application.auth.data

import com.b1nd.dauthserver.domain.user.entity.UserEntity
import com.b1nd.dauthserver.domain.user.enumeration.RoleType
import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

@Schema(description = "ID/PW 로그인 요청")
data class IdLoginRequest(
    @field:NotBlank
    @Schema(description = "도담도담 아이디", example = "hong123")
    val id: String,

    @field:NotBlank
    @Schema(description = "도담도담 비밀번호", example = "password123")
    val password: String,

    @field:NotBlank
    @Schema(description = "OAuth 클라이언트 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: String,

    @field:NotBlank
    @Schema(description = "로그인 후 리다이렉트될 URL", example = "https://myapp.com/callback")
    val redirectUrl: String,

    @field:NotEmpty
    @Schema(description = "요청할 scope 목록", example = "[\"OPENID\", \"READ_PROFILE\"]")
    val scopes: List<ScopeType>
) {
    fun toEntity(refreshToken: String, role: RoleType) =
        UserEntity(
            dodamId = id,
            client = clientId,
            scopes = scopes,
            role = role,
            refreshToken = refreshToken
        )
}