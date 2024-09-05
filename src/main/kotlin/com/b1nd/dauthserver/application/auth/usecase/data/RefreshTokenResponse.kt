package com.b1nd.dauthserver.application.auth.usecase.data

import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.JwtProperties

data class RefreshTokenResponse(
    val accessToken: String,
    val tokenType: String,
    val expiredIn: Int
) {
    companion object {
        fun of(accessToken: String, jwtProperties: JwtProperties)=
            RefreshTokenResponse(
                accessToken,
                "bearer",
                jwtProperties.accessExpire
            )
        }
    }

