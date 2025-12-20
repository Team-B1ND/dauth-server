package com.b1nd.dauthserver.application.token

import com.b1nd.dauthserver.application.token.data.StandardTokenResponse
import com.b1nd.dauthserver.application.token.data.TokenRequest
import com.b1nd.dauthserver.domain.app.exception.ApplicationKeyNotMatchException
import com.b1nd.dauthserver.domain.app.exception.ApplicationNotFoundException
import com.b1nd.dauthserver.domain.app.service.ApplicationService
import com.b1nd.dauthserver.domain.oauth.exception.OAuth2Exception
import com.b1nd.dauthserver.domain.user.exception.UserNotFoundException
import com.b1nd.dauthserver.domain.user.service.UserService
import com.b1nd.dauthserver.infrastructure.database.redis.enumeration.RedisKeyType
import com.b1nd.dauthserver.infrastructure.database.redis.exception.RedisKeyNotFoundException
import com.b1nd.dauthserver.infrastructure.database.redis.service.RedisService
import com.b1nd.dauthserver.infrastructure.security.properties.InternalProperties
import com.b1nd.dauthserver.infrastructure.security.token.core.TokenProvider
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.Base64

@Component
@Transactional(rollbackFor = [Exception::class])
class TokenUseCase(
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
    private val redisService: RedisService,
    private val applicationService: ApplicationService,
    private val internalProperties: InternalProperties
) {
    suspend fun issue(request: TokenRequest): StandardTokenResponse {
        try {
            val (clientId, clientSecret) = resolveCredentials(
                request.clientId,
                request.clientSecret,
                request.authorization
            )

            return when (request.grantType) {
                "authorization_code" -> {
                    if (request.code.isNullOrBlank()) {
                        throw OAuth2Exception.invalidRequest("code is required for authorization_code grant")
                    }
                    issueToken(request.code, clientId, clientSecret)
                }
                "refresh_token" -> {
                    if (request.refreshToken.isNullOrBlank()) {
                        throw OAuth2Exception.invalidRequest("refresh_token is required for refresh_token grant")
                    }
                    refreshToken(request.refreshToken, clientId, clientSecret)
                }
                else -> throw OAuth2Exception.unsupportedGrantType("Unsupported grant_type: ${request.grantType}")
            }
        } catch (e: OAuth2Exception) {
            throw e
        } catch (e: ApplicationNotFoundException) {
            throw OAuth2Exception.invalidClient("Invalid client_id")
        } catch (e: ApplicationKeyNotMatchException) {
            throw OAuth2Exception.invalidClient("Invalid client credentials")
        } catch (e: UserNotFoundException) {
            throw OAuth2Exception.invalidGrant("User not found")
        } catch (e: RedisKeyNotFoundException) {
            throw OAuth2Exception.invalidGrant("Invalid or expired authorization code")
        } catch (e: Exception) {
            throw OAuth2Exception.serverError(e.message)
        }
    }

    private fun resolveCredentials(
        clientId: String?,
        clientSecret: String?,
        authorization: String?
    ): Pair<String, String> {
        if (!clientId.isNullOrBlank() && !clientSecret.isNullOrBlank()) {
            return clientId to clientSecret
        }

        if (!authorization.isNullOrBlank() && authorization.startsWith("Basic ")) {
            val decoded = String(Base64.getDecoder().decode(authorization.substring(6)))
            val parts = decoded.split(":", limit = 2)
            if (parts.size == 2) {
                return parts[0] to parts[1]
            }
        }

        throw OAuth2Exception.invalidClient("Client credentials are required")
    }

    private suspend fun issueToken(code: String, clientId: String, clientSecret: String): StandardTokenResponse {
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

    private suspend fun refreshToken(refreshToken: String, clientId: String, clientSecret: String): StandardTokenResponse {
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
