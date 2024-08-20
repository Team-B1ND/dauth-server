package com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.ClientEntity
import reactor.core.publisher.Mono

interface CustomClientRepository {
    fun saveClient(client: ClientEntity): Mono<ClientEntity>
}