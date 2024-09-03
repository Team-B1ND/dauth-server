package com.b1nd.dauthserver.infrastructure.adapter.driven.client.token

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.token")
data class TokenProperties(
    val tokenEndpoint: String
)
