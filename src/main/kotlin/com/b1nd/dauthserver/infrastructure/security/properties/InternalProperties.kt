package com.b1nd.dauthserver.infrastructure.security.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.internal")
data class InternalProperties(
    val clientId: String
)
