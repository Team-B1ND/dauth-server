package com.b1nd.dauthserver.application.support.outport

import com.b1nd.dauthserver.domain.auth.model.TokenInfo
import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.JwtClaim
import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.JwtPayload

interface JwtPort {
    fun issue(payload: JwtClaim): TokenInfo
    fun provide(claim: JwtClaim, subject: String, expire: Int): String
    fun getVerifyKey(): String
    fun getIssuer(): String
    fun parse(token: String): JwtPayload
    fun matchIssuer(iss: String): Boolean
    fun matchSecret(secret: String): Boolean
    fun getAccessExpire(): Int
}