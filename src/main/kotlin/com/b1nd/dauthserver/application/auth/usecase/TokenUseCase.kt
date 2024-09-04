package com.b1nd.dauthserver.application.auth.usecase

import com.b1nd.dauthserver.application.auth.exception.InvalidCodeException
import com.b1nd.dauthserver.application.support.outport.JwtPort
import com.b1nd.dauthserver.application.support.outport.RedisPort
import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.domain.auth.model.TokenInfo
import com.b1nd.dauthserver.domain.client.model.ClientCredentials
import com.b1nd.dauthserver.domain.user.outport.UserPort
import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.JwtPayload
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TokenUseCase(
    private val redisPort: RedisPort,
    private val userPort: UserPort,
    private val jwtPort: JwtPort
) {

    fun generateToken(clientCredentials: ClientCredentials): Mono<ResponseData<TokenInfo>> {
        return redisPort.getData(clientCredentials.code)
            .switchIfEmpty(Mono.error(InvalidCodeException))
            .flatMap { userUnique ->
                userPort.getByUserUniqueAndClientId(userUnique.toString(), clientCredentials.clientId)
                    .flatMap { user ->
                        val token = jwtPort.issue(JwtPayload.of(user, clientCredentials.clientId))
                        redisPort.deleteData(clientCredentials.code)
                            .then(Mono.just(ResponseData.ok("토큰 발급 완료", token)))
                    }
            }
    }
}
