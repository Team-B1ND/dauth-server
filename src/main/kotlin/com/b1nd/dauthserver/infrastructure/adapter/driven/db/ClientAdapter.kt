package com.b1nd.dauthserver.infrastructure.adapter.driven.db

import com.b1nd.dauthserver.application.client.outport.ClientPort
import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.client.model.ClientInfo
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.ClientEntity
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository.ClientRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Component
class ClientAdapter(private val clientRepository: ClientRepository) : ClientPort{

    override fun getByClientInfo(clientInfo: ClientInfo): Mono<ClientEntity?> {
        return clientRepository.findFirstByRedirectUrlAndClientUrlAndClientName(clientInfo.redirectUrl,clientInfo.clientUrl,clientInfo.clientName)
    }

    override fun saveClient(client: Client): Mono<Client> {
        return clientRepository.saveClient(client.toEntity())
            .map { client }
    }

    override fun getRandomValuesUpToThree(): Flux<Client> {
        return clientRepository.findRandomLimitThree()
            .flatMap { c ->
                Mono.fromCallable { Client.fromEntity(c) }
                    .subscribeOn(Schedulers.boundedElastic())  // 비동기 스레드 풀에서 실행
            }
    }

    override fun getById(clientId: String): Mono<Client> {
        return clientRepository.findById(clientId)
            .map { c -> Client.fromEntity(c) }
    }

    override fun getByDodamId(dodamId: String): Flux<Client?> {
        return clientRepository.findAllByDodamId(dodamId)
            .flatMap { clientEntity ->
                Mono.justOrEmpty(clientEntity)
                    .map { Client.fromEntity(it) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
    }

    override fun deleteById(clientId: String): Mono<Void> {
        return clientRepository.deleteById(clientId)
    }

}