package com.b1nd.dauthserver.infrastructure.adapter.driving

import com.b1nd.dauthserver.application.auth.usecase.TokenUseCase
import com.b1nd.dauthserver.application.auth.usecase.data.RefreshTokenRequest
import com.b1nd.dauthserver.domain.auth.model.Token
import com.b1nd.dauthserver.domain.client.model.ClientCredentials
import org.springframework.web.bind.annotation.PathVariable
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

    @PostMapping("/verify/{secret}")
    fun verify(@PathVariable secret: String, @RequestBody token: Token) =
        tokenUseCase.verify(secret, token.token)

    @PostMapping("/refresh")
    fun refresh(@RequestBody refreshTokenRequest: RefreshTokenRequest) =
        tokenUseCase.refreshToken(
            refreshToken = refreshTokenRequest.refreshToken,
            clientId = refreshTokenRequest.clientId
        )
}