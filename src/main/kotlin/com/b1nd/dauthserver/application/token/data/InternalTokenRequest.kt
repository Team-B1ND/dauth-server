package com.b1nd.dauthserver.application.token.data

data class InternalTokenRequest(
    val grantType: String,
    val clientId: String,
    val code: String? = null,
    val refreshToken: String? = null
)
