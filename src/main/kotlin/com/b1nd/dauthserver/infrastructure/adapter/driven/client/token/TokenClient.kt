package com.b1nd.dauthserver.infrastructure.adapter.driven.client.token

import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient

@Component
class TokenClient(
    private val webclient: WebClient,
    private val tokenProperties: TokenProperties
) {

    fun validate(token: String) =
        webclient.post()
            .uri(tokenProperties.tokenEndpoint+"/auth/token/verify")
            .bodyValue(TokenRequest(token))
            .retrieve()
            .onStatus(HttpStatusCode::isError) { response: ClientResponse ->
                throw TokenException(response.statusCode().value())
            }
            .bodyToMono(TokenResponse::class.java);
}