package com.b1nd.dauthserver.infrastructure.adapter.driving

import com.b1nd.dauthserver.application.auth.usecase.TokenUseCase
import com.b1nd.dauthserver.domain.client.model.ClientCredentials
import com.b1nd.dauthserver.domain.client.model.ClientKey
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenRouter(
    private val tokenUseCase: TokenUseCase
) {
    @PostMapping
    fun generate(@RequestBody clientCredentials: ClientCredentials)=
        tokenUseCase.generateToken(clientCredentials)
}