package com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam

import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class DodamClient(
    private val webClient: WebClient,
    private val dodamProperties: DodamProperties
) {
    fun dodamLogin(dodamLoginRequest: DodamLoginRequest): Mono<DodamLoginResponse> {
        return webClient.post()
            .uri(dodamProperties.loginEndpoint+"/auth/login+?key="+dodamProperties.key)
            .bodyValue(dodamLoginRequest)
            .retrieve()
            .onStatus(HttpStatusCode::isError) { response: ClientResponse ->
                throw DodamClientException(response.statusCode().value())
            }
            .bodyToMono(DodamLoginResponse::class.java);
    }
}