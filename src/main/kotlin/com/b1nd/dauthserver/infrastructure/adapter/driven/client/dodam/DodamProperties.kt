package com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.dodam")
data class DodamProperties(
    val key: String,
    val loginEndpoint: String,
    val tokenEndpoint: String
)