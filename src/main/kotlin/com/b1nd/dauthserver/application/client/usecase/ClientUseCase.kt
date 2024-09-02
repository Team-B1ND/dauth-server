package com.b1nd.dauthserver.application.client.usecase

import com.b1nd.dauthserver.application.client.outport.ClientPort
import com.b1nd.dauthserver.application.common.ResponseData
import com.b1nd.dauthserver.application.common.util.UUIDUtil
import com.b1nd.dauthserver.domain.client.exception.ExistsClientException
import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.client.model.ClientFullInfo
import com.b1nd.dauthserver.domain.client.model.ClientInfo
import com.b1nd.dauthserver.infrastructure.adapter.driven.security.UserAuthHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Component
class ClientUseCase(
    private val clientPort: ClientPort,
    private val userAuthHolder: UserAuthHolder
) {

    @Transactional
    fun appendClient(clientFullInfo: ClientFullInfo, issuerId: String): Mono<ResponseData<Client>> {
        return clientPort.getByClientInfo(clientFullInfo.clientInfo)
            .flatMap {
                // 클라이언트가 이미 존재하는 경우 예외를 반환
                Mono.error<ResponseData<Client>>(ExistsClientException())
            }
            .switchIfEmpty(
                // 클라이언트가 존재하지 않는 경우 클라이언트를 등록하고 응답 생성
                addClient(issuerId, clientFullInfo.clientInfo)
                    .map { client ->
                        ResponseData.ok("클라이언트 등록 성공", client)
                    }
            )
    }

    private fun addClient(issuerId: String, clientInfo: ClientInfo): Mono<Client> {
        return UUIDUtil.generateClientKey()
            .flatMap { clientKey ->
                val newClient = Client(
                    issuer = issuerId,
                    clientName = clientInfo.clientName,
                    clientUrl = clientInfo.clientUrl,
                    redirectUrl = clientInfo.redirectUrl,
                    clientKey = clientKey
                )
                clientPort.saveClient(newClient)
            }
    }

    @Transactional
    fun readRandomClient(): Mono<ResponseData<Flux<ClientInfo>>> {
        return Mono.just(
            ResponseData.ok(
                "랜덤으로 클라이언트 조회 완료",
                clientPort.getRandomValuesUpToThree()
                    .flatMap { client ->
                        Mono.fromCallable { client.toClientInfo() }
                            .subscribeOn(Schedulers.boundedElastic())
                    }
            )
        )
    }


    @Transactional
    fun readMyClient(): Mono<ResponseData<Flux<ClientInfo>>> {
        return userAuthHolder.current()
            .flatMap { user ->
                val clientInfoFlux = clientPort.getByDodamId(user.dodamId)
                    .filter { it != null }
                    .map { it!!.toClientInfo() }
                    .subscribeOn(Schedulers.boundedElastic())

                Mono.just(ResponseData.ok("내 클라이언트 조회 완료", clientInfoFlux))
            }
    }


    @Transactional
    fun readById(clientId: String): Mono<ResponseData<Client>> {
        return clientPort.getById(clientId)
            .map { client -> ResponseData.ok("id로 클라이언트 조회 완료", client) }
    }
}
