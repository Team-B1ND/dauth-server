package com.b1nd.dauthserver.application.auth.usecase

import com.b1nd.dauthserver.application.auth.exception.InvalidCodeException
import com.b1nd.dauthserver.application.auth.exception.InvalidSecretException
import com.b1nd.dauthserver.application.auth.usecase.data.RefreshTokenResponse
import com.b1nd.dauthserver.application.support.outport.JwtPort
import com.b1nd.dauthserver.application.support.outport.RedisPort
import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.domain.auth.model.TokenInfo
import com.b1nd.dauthserver.domain.client.model.ClientCredentials
import com.b1nd.dauthserver.domain.user.outport.UserPort
import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.InvalidTokenException
import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.JwtClaim
import com.b1nd.dauthserver.infrastructure.adapter.driven.jwt.JwtPayload
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TokenUseCase(
    private val redisPort: RedisPort,
    private val userPort: UserPort,
    private val jwtPort: JwtPort
) {

    fun generateToken(clientCredentials: ClientCredentials) =
        redisPort.getData(clientCredentials.code)
            .switchIfEmpty(Mono.error(InvalidCodeException))
            .flatMap { userUnique ->
                userPort.getByUserUniqueAndClientId(userUnique.toString(), clientCredentials.clientId)
                    .flatMap { user ->
                        val token = jwtPort.issue(JwtClaim.of(user, clientCredentials.clientId))
                        redisPort.deleteData(clientCredentials.code)
                            .then(Mono.just(ResponseData.ok("토큰 발급 완료", token)))
                    }
            }


    fun verify(secret: String, token: String) =
        if (!jwtPort.matchSecret(secret)) {
            Mono.error(InvalidSecretException)
        } else {
            Mono.fromCallable { ResponseData.ok("토큰 검증 완료", jwtPort.parse(token)) }
        }


    fun refreshToken(refreshToken: String, clientId: String): Mono<ResponseData<RefreshTokenResponse>> {
        val decoded = jwtPort.parse(refreshToken)

        if (!jwtPort.matchIssuer(decoded.iss) || decoded.sub != "refreshToken") {
            return Mono.error(InvalidTokenException)
        }

        return userPort.getByUserUniqueAndClientId(decoded.userUnique, clientId)
            .map { user ->
                val accessToken = jwtPort.provide(
                    JwtClaim.of(user, clientId),
                    "token",
                    jwtPort.getAccessExpire()
                )
                ResponseData.ok(
                    "토큰 재발급 완료",
                    RefreshTokenResponse(accessToken, "bearer", jwtPort.getAccessExpire())
                )
            }
    }





}