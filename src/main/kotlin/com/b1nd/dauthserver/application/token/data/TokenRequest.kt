package com.b1nd.dauthserver.application.token.data

data class TokenRequest(
    val grantType: String,
    val code: String?,
    val redirectUri: String?,
    val clientId: String?,
    val clientSecret: String?,
    val refreshToken: String?,
    val codeVerifier: String?,
    val authorization: String?
)
