package com.b1nd.dauthserver.domain.auth.model

data class TokenInfo(
    val accessToken: String,
    val refreshToken: String,
    val accessExpire: Int,
    val refreshExpire: Int,
    val tokenType: String
)