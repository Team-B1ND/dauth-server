package com.b1nd.dauthserver.application.client.usecase

import com.b1nd.dauthserver.application.client.outport.ClientPort
import com.b1nd.dauthserver.application.common.ResponseData
import com.b1nd.dauthserver.application.common.util.UUIDUtil
import com.b1nd.dauthserver.domain.client.exception.ExistsClientException
import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.client.model.ClientFullInfo
import com.b1nd.dauthserver.domain.client.model.ClientInfo
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Component
class ClientUseCase(private val clientPersistencePort: ClientPort) {

    @Transactional
    fun appendClient(clientFullInfo: ClientFullInfo, issuerId: String): Mono<ResponseData<Client>> {
        return clientPersistencePort.getByClientInfo(clientFullInfo.clientInfo)
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
                clientPersistencePort.saveClient(newClient)
            }
    }

    fun readRandomClient(): Mono<ResponseData<List<ClientInfo>>> {
        return clientPersistencePort.getRandomValuesUpToThree()
            .collectList()
            .map { clients ->
                val clientInfos = clients.map { client ->
                    client.toClientInfo()
                }
                ResponseData.ok("랜덤으로 클라이언트 조회 완료",clientInfos)
            }
    }

}
