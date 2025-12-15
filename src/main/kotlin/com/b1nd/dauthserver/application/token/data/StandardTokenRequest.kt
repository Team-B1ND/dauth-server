package com.b1nd.dauthserver.application.token.data

data class StandardTokenRequest(
    val grantType: String? = null,
    val code: String? = null,
    val redirectUri: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null,
    val refreshToken: String? = null
)
