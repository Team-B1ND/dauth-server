package com.b1nd.dauthserver.infrastructure.adapter.driven.client.token

data class TokenResponse(
    val memberId: String,
    val accessLevel: Int,
    val apiKeyAccessLevel: Int
)