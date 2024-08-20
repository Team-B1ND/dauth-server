package com.b1nd.dauthserver.domain.client.model

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.ClientEntity

data class Client(
    val clientKey: ClientKey,
    val clientName: String,
    val clientUrl: String,
    val redirectUrl: String,
    val issuer: String
){

    fun toClientInfo() = ClientInfo(clientName,clientUrl,redirectUrl)
    fun toEntity() = ClientEntity(
        clientId = clientKey.clientId,
        clientSecret = clientKey.clientSecret,
        clientUrl = clientUrl,
        clientName = clientName,
        redirectUrl = redirectUrl,
        dodamId = issuer
    )
    companion object {
        fun fromEntity(clientEntity: ClientEntity) =
            Client(
                clientKey = ClientKey(
                    clientId = clientEntity.clientId,
                    clientSecret = clientEntity.clientSecret
                ),
                issuer = clientEntity.dodamId,
                clientName = clientEntity.clientName,
                clientUrl = clientEntity.clientUrl,
                redirectUrl = clientEntity.redirectUrl
            )
    }
}