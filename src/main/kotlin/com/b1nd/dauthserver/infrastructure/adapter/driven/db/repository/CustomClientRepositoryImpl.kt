package com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository;

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.ClientEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CustomClientRepositoryImpl(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
): CustomClientRepository{
    override fun saveClient(client: ClientEntity): Mono<ClientEntity> {
        return r2dbcEntityTemplate.insert(ClientEntity::class.java)
            .using(client)
    }
}
