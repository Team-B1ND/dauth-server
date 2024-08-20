package com.b1nd.dauthserver.infrastructure.adapter.driving

import com.b1nd.dauthserver.application.client.usecase.ClientUseCase
import com.b1nd.dauthserver.application.common.ResponseData
import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.client.model.ClientFullInfo
import com.b1nd.dauthserver.domain.client.model.ClientInfo
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/client")
class ClientRouter(val clientUseCase: ClientUseCase) {
    @PostMapping("/register")
    fun registerClient(@RequestBody client: ClientFullInfo, @RequestParam issuerId: String):Mono<ResponseData<Client>>{
        return clientUseCase.appendClient(client, issuerId)
    }

    @GetMapping("/random")
    fun getRandomClients(): Mono<ResponseData<List<ClientInfo>>>{
        return clientUseCase.readRandomClient()
    }
}