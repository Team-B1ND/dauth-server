package com.b1nd.dauthserver.application.client.outport

import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.client.model.ClientInfo
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.ClientEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ClientPort {
    fun getByClientInfo(clientInfo: ClientInfo): Mono<ClientEntity?>
    fun saveClient(client: Client): Mono<Client>
    fun getRandomValuesUpToThree(): Flux<Client>
    fun getById(clientId: String): Mono<Client>
}