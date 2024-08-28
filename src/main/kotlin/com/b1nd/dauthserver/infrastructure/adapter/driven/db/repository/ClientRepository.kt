package com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.ClientEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ClientRepository: R2dbcRepository<ClientEntity, String>, CustomClientRepository {
    fun findFirstByRedirectUrlAndClientUrlAndClientName(redirectUrl: String, clientUrl: String, clientName: String): Mono<ClientEntity?>

    @Query("SELECT * FROM client ORDER BY RAND() LIMIT 3")
    fun findRandomLimitThree(): Flux<ClientEntity>

    fun findAllByDodamId(dodamId: String): Flux<ClientEntity?>
}