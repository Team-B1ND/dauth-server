package com.b1nd.dauthserver.application.support.outport

import com.b1nd.dauthserver.domain.auth.model.TokenInfo
import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.JwtPayload

interface JwtPort {
    fun issue(payload: JwtPayload): TokenInfo
}