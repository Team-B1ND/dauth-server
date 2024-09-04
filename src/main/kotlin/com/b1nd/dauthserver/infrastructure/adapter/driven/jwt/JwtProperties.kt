package com.b1nd.dauthserver.infrastructure.adapter.driven.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.jwt")
data class JwtProperties (
    val secret: String,
    val verify: String,
    val issuer: String,
    val accessExpire: Int,
    val refreshExpire: Int
)