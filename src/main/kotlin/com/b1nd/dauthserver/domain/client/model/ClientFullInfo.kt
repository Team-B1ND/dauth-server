package com.b1nd.dauthserver.domain.client.model

data class ClientFullInfo(
    val clientInfo: ClientInfo,
    val frontEnd: String,
    val backEnd: String,
)