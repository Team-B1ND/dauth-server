package com.b1nd.dauthserver.application.auth.data

import com.b1nd.dauthserver.domain.user.entity.UserEntity
import com.b1nd.dauthserver.domain.user.enumeration.RoleType
import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "앱 자체 로그인 요청")
data class AppLoginRequest(
    @Schema(description = "OAuth 클라이언트 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: String,

    @Schema(description = "OAuth 클라이언트 Secret", example = "secret-key-12345")
    val clientSecret: String,

    @Schema(description = "도담도담 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val access: String,

    @Schema(description = "도담도담 Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val refresh: String
) {
    fun toEntity(dodamId: String, role: RoleType, scopes: List<ScopeType>) =
        UserEntity(
            dodamId = dodamId,
            client = clientId,
            scopes = scopes,
            role = role,
            refreshToken = refresh
        )
}
