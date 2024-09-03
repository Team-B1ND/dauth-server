package com.b1nd.dauthserver.application.auth.usecase

import com.b1nd.dauthserver.infrastructure.adapter.driven.client.token.TokenClient
import org.springframework.stereotype.Component

@Component
class TokenUseCase(
    private val tokenClient: TokenClient
) {
    fun token
}