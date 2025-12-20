package com.b1nd.dauthserver.application.token

import com.b1nd.dauthserver.application.token.data.StandardTokenResponse
import com.b1nd.dauthserver.domain.app.service.ApplicationService
import com.b1nd.dauthserver.domain.user.exception.UserNotFoundException
import com.b1nd.dauthserver.domain.user.service.UserService
import com.b1nd.dauthserver.infrastructure.database.redis.enumeration.RedisKeyType
import com.b1nd.dauthserver.infrastructure.database.redis.service.RedisService
import com.b1nd.dauthserver.infrastructure.security.properties.InternalProperties
import com.b1nd.dauthserver.infrastructure.security.token.core.TokenProvider
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class TokenUseCase(
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
    private val redisService: RedisService,
    private val applicationService: ApplicationService,
    private val internalProperties: InternalProperties
) {
    suspend fun issueToken(code: String, clientId: String, clientSecret: String): StandardTokenResponse {
        val userId = redisService.get(RedisKeyType.LOGIN_TOKEN, code)
        val user = userService.getById(userId.toLong()) ?: throw UserNotFoundException()
        val application = applicationService.getByClientIdAndSecret(clientId, clientSecret)
        val accessToken = tokenProvider.generateAccessToken(user.dodamId, application.clientId)
        val refreshToken = tokenProvider.generateRefreshToken(user.dodamId, application.clientId)
        val idToken = tokenProvider.generateIdToken(application.clientId, user.role, application.url, user.dodamId, application.clientSecret)
        redisService.delete(RedisKeyType.LOGIN_TOKEN, code)
        val scopes = user.scopes.joinToString(" ") { it.value }
        return StandardTokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            idToken = idToken,
            scope = scopes
        )
    }

    suspend fun refreshToken(refreshToken: String, clientId: String, clientSecret: String): StandardTokenResponse {
        applicationService.getByClientIdAndSecret(clientId, clientSecret)
        val newAccessToken = tokenProvider.reissueAccessToken(refreshToken)
        return StandardTokenResponse(
            accessToken = newAccessToken,
            refreshToken = refreshToken
        )
    }

    suspend fun issueTokenInternal(code: String, clientId: String): StandardTokenResponse {
        require(clientId == internalProperties.clientId) { "Unauthorized client for internal token endpoint" }

        val userId = redisService.get(RedisKeyType.LOGIN_TOKEN, code)
        val user = userService.getById(userId.toLong()) ?: throw UserNotFoundException()
        val application = applicationService.getByClientId(clientId)

        val accessToken = tokenProvider.generateAccessToken(user.dodamId, application.clientId)
        val refreshToken = tokenProvider.generateRefreshToken(user.dodamId, application.clientId)
        val idToken = tokenProvider.generateIdToken(application.clientId, user.role, application.url, user.dodamId, application.clientSecret)

        redisService.delete(RedisKeyType.LOGIN_TOKEN, code)
        val scopes = user.scopes.joinToString(" ") { it.value }

        return StandardTokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            idToken = idToken,
            scope = scopes
        )
    }

    suspend fun refreshTokenInternal(refreshToken: String, clientId: String): StandardTokenResponse {
        require(clientId == internalProperties.clientId) { "Unauthorized client for internal token endpoint" }

        applicationService.getByClientId(clientId)
        val newAccessToken = tokenProvider.reissueAccessToken(refreshToken)

        return StandardTokenResponse(
            accessToken = newAccessToken,
            refreshToken = refreshToken
        )
    }
}