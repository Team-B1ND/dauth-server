package com.b1nd.dauthserver.infrastructure.client.dodam.data

data class DodamLoginResponse(
    val member: MemberResponse,
    val accessToken: String,
    val refreshToken: String
)