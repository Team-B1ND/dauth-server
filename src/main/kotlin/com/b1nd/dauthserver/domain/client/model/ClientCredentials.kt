package com.b1nd.dauthserver.domain.client.model

data class ClientCredentials(
    val code: String,
    val clientId: String,
    val clientSecret: String
)
