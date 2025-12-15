package com.b1nd.dauthserver.infrastructure.security.token.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.token")
data class TokenProperties(
    val key: String,
    val expire: Long = 3600000 // 1시간 기본값
)
