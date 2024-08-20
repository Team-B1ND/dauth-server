package com.b1nd.dauthserver.domain.client.model

data class ClientInfo(
    val clientName: String,
    val clientUrl: String,
    val redirectUrl: String
)