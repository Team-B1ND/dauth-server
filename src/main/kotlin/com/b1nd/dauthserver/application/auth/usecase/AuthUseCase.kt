package com.b1nd.dauthserver.application.auth.usecase

import com.b1nd.dauthserver.application.client.exception.NotFoundClientIdException
import com.b1nd.dauthserver.application.client.exception.UnauthorizedRedirectUrlException
import com.b1nd.dauthserver.application.client.outport.ClientPort
import com.b1nd.dauthserver.application.common.ResponseData
import com.b1nd.dauthserver.domain.auth.model.DauthLoginInfo
import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.user.model.User
import com.b1nd.dauthserver.domain.user.model.UserCredentials
import com.b1nd.dauthserver.domain.user.model.UserWithCode
import com.b1nd.dauthserver.domain.user.outport.UserPort
import com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam.DodamClient
import com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam.DodamLoginResponse
import com.b1nd.dauthserver.infrastructure.adapter.driven.redis.RedisService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class AuthUseCase(
    private val dodamClient: DodamClient,
    private val clientPort: ClientPort,
    private val userPort: UserPort,
    private val redisService: RedisService
) {

    fun login(userCredentials: UserCredentials): Mono<ResponseData<DauthLoginInfo>> {
        return dodamClient.dodamLogin(userCredentials.toDodamLoginRequest())
            .flatMap { dodamLoginInfo ->
                validateClient(userCredentials)
                    .flatMap { client ->
                        findOrSaveUser(client, userCredentials.id, dodamLoginInfo)
                    }
                    .flatMap { userWithCode ->
                        redisService.saveData(userWithCode.code, userWithCode.user.userUnique)
                            .then(Mono.just(createSuccessResponse(userWithCode, dodamLoginInfo, userCredentials.redirectUrl)))
                    }
            }
    }

    private fun validateClient(userCredentials: UserCredentials): Mono<Client> {
        return clientPort.getById(userCredentials.clientId)
            .switchIfEmpty(Mono.error(NotFoundClientIdException))
            .flatMap { client ->
                if (client.redirectUrl != userCredentials.redirectUrl) {
                    Mono.error(UnauthorizedRedirectUrlException)
                } else {
                    Mono.just(client)
                }
            }
    }

    private fun findOrSaveUser(client: Client, dodamId: String, dodamLoginInfo: DodamLoginResponse): Mono<UserWithCode> {
        val code = UUID.randomUUID().toString()
        return userPort.getByDodamIdAndClientId(dodamId, client.clientKey.clientId)
            .switchIfEmpty(
                userPort.saveUser(
                    User(UUID.randomUUID(), dodamLoginInfo.member.id, client.clientKey.clientId)
                )
            )
            .map { user -> UserWithCode(user!!, code) }
    }

    private fun createSuccessResponse(userWithCode: UserWithCode, dodamLoginInfo: DodamLoginResponse, redirectUrl: String): ResponseData<DauthLoginInfo> {
        val dauthLoginInfo = DauthLoginInfo.of(userWithCode.code, dodamLoginInfo.member, redirectUrl)
        return ResponseData.ok("dauth 로그인 성공", dauthLoginInfo)
    }
}
