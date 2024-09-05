package com.b1nd.dauthserver.application.auth.usecase.data

data class RefreshTokenRequest(
    val refreshToken: String,
    val clientId: String
)
