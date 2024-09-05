package com.b1nd.dauthserver.infrastructure.adapter.driven.jwt

data class JwtPayload(
    val userUnique: String,
    val dodamId: String,
    val clientId: String,
    val iss: String,
    val sub: String
)
