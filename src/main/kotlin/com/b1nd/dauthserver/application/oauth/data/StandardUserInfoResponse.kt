package com.b1nd.dauthserver.application.oauth.data

import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import com.b1nd.dauthserver.infrastructure.client.dodam.data.MemberResponse
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class StandardUserInfoResponse(
    val sub: String,
    val name: String? = null,
    val email: String? = null,

    @JsonProperty("profile_image")
    val profileImage: String? = null,

    val role: String? = null,
    val phone: String? = null
) {
    companion object {
        fun fromMember(member: MemberResponse, scopes: List<ScopeType>): StandardUserInfoResponse {
            val hasOpenId = scopes.contains(ScopeType.OPENID)
            val hasProfile = scopes.contains(ScopeType.READ_PROFILE)
            val hasPhone = scopes.contains(ScopeType.PHONE)

            return StandardUserInfoResponse(
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
