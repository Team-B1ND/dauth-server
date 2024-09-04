package com.b1nd.dauthserver.infrastructure.adapter.driving

import com.b1nd.dauthserver.application.client.usecase.ClientUseCase
import com.b1nd.dauthserver.application.support.response.Response
import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.client.model.ClientFullInfo
import com.b1nd.dauthserver.domain.client.model.ClientInfo
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/client")
class ClientRouter(val clientUseCase: ClientUseCase) {
    @PostMapping("/register")
    fun registerClient(@RequestBody client: ClientFullInfo): Mono<ResponseData<Client>>{
        return clientUseCase.appendClient(client)
    }

    @GetMapping("/random")
    fun getRandomClients(): Mono<ResponseData<Flux<ClientInfo>>>{
        return clientUseCase.readRandomClient()
    }

    @GetMapping
    fun getClients(): Mono<ResponseData<Flux<ClientInfo>>>{
        return clientUseCase.readMyClient()
    }

    @GetMapping("/{clientId}")
    fun getClientById(@PathVariable clientId: String): Mono<ResponseData<Client>> {
        return clientUseCase.readById(clientId)
    }

    @PutMapping("/{clientId}")
    fun modifyById(
        @PathVariable clientId: String,
        @RequestBody clientInfo: ClientInfo
    ): Mono<Response> {
        return clientUseCase.modifyById(clientId, clientInfo)
    }

    @DeleteMapping("/{clientId}")
    fun removeById(@PathVariable clientId: String): Mono<Response>{
        return clientUseCase.removeById(clientId)
    }
}