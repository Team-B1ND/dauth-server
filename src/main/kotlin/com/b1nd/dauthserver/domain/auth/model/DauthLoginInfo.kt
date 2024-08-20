package com.b1nd.dauthserver.domain.auth.model

import com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam.DodamLoginResponse

data class DauthLoginInfo(
    val name: String,
    val profileImage: String?,
    val location: String
) {
    companion object {
        fun of(code: String, member: DodamLoginResponse.Member, redirectUrl: String) = DauthLoginInfo(
            name = member.name,
            profileImage = member.profileImage,
            location = "${redirectUrl}?code=${code}&state=${member.status}"
        )
    }
}