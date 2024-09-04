package com.b1nd.dauthserver.application.auth.usecase

import com.b1nd.dauthserver.application.client.outport.ClientPort
import com.b1nd.dauthserver.application.support.outport.RedisPort
import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.domain.auth.model.DauthLoginInfo
import com.b1nd.dauthserver.domain.user.model.UserCredentials
import com.b1nd.dauthserver.domain.user.outport.UserPort
import com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam.DodamClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class AuthUseCase(
    private val dodamClient: DodamClient,
    private val clientPort: ClientPort,
    private val userPort: UserPort,
    private val redisPort: RedisPort
) {

    fun login(userCredentials: UserCredentials): Mono<ResponseData<DauthLoginInfo>> {
        return dodamClient.dodamLogin(userCredentials.toDodamLoginRequest())
            .flatMap { dodamLoginInfo ->
                clientPort.validateClient(userCredentials)
                    .flatMap { client ->
                        userPort.findOrSaveUser(client, userCredentials.id, dodamLoginInfo)
                    }
                    .flatMap { user ->
                        val code = UUID.randomUUID().toString()
                        redisPort.saveData(code, user.userUnique)
                            .then(
                                Mono.just(
                                    ResponseData.ok(
                                        "dauth 로그인 성공",
                                        DauthLoginInfo.of(code, dodamLoginInfo.member, userCredentials.redirectUrl)
                                    )
                                )
                            )
                    }
            }
    }

}
