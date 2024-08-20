package com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("client")
class ClientEntity (
    clientId: String,
    clientSecret: String,
    dodamId: String,
    clientName: String,
    clientUrl: String,
    redirectUrl: String
){
    @Id
    var clientId = clientId
        private set

    var clientSecret = clientSecret
        private set

    var dodamId = dodamId
        private set

    var clientName = clientName
        private set

    var clientUrl = clientUrl
        private set

    var redirectUrl = redirectUrl
        private set
}