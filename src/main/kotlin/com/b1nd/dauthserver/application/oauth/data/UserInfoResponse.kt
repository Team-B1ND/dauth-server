package com.b1nd.dauthserver.application.oauth.data

import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import com.b1nd.dauthserver.infrastructure.client.dodam.data.MemberResponse
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 정보 응답 (scope에 따라 포함되는 필드가 달라짐)")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserInfoResponse(
    @Schema(description = "사용자 고유 식별자 (항상 포함)", example = "550e8400-e29b-41d4-a716-446655440000")
    val sub: String,

    @Schema(description = "사용자 이름 (openid, read:profile scope 필요)", example = "홍길동")
    val name: String? = null,

    @Schema(description = "이메일 주소 (read:profile scope 필요)", example = "hong@dsm.hs.kr")
    val email: String? = null,

    @Schema(description = "프로필 이미지 URL (read:profile scope 필요)", example = "https://dodam.b1nd.com/profile/123.jpg")
    val profileImage: String? = null,

    @Schema(description = "사용자 역할 (read:profile scope 필요)", example = "STUDENT")
    val role: String? = null,

    @Schema(description = "전화번호 (phone scope 필요)", example = "010-1234-5678")
    val phone: String? = null
) {
    companion object {
        fun of(member: MemberResponse, scopes: List<ScopeType>): UserInfoResponse {
            val hasOpenId = scopes.contains(ScopeType.OPENID)
            val hasProfile = scopes.contains(ScopeType.READ_PROFILE)
            val hasPhone = scopes.contains(ScopeType.PHONE)

            return UserInfoResponse(
                sub = member.id,
                name = if (hasOpenId || hasProfile) member.name else null,
                email = if (hasProfile) member.email else null,
                profileImage = if (hasProfile) member.profileImage else null,
                role = if (hasProfile) member.role.name else null,
                phone = if (hasPhone) member.phone else null
            )
        }
    }
}
