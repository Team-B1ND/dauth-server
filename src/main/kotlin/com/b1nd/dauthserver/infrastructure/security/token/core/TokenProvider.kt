package com.b1nd.dauthserver.infrastructure.security.token.core

import com.b1nd.dauthserver.domain.user.enumeration.RoleType
import com.b1nd.dauthserver.infrastructure.security.token.properties.TokenProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.lang.System.currentTimeMillis
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class TokenProvider(
    private val properties: TokenProperties,
) {
    companion object {
        private const val ACCESS_TOKEN_EXPIRE = 1000L * 60 * 60 // 1시간
        private const val REFRESH_TOKEN_EXPIRE = 1000L * 60 * 60 * 24 * 14 // 14일
    }

    fun generateAccessToken(memberId: String, clientId: String): String =
        Jwts.builder()
            .claim("memberId", memberId)
            .claim("clientId", clientId)
            .claim("type", "access")
            .issuedAt(Date(currentTimeMillis()))
            .expiration(Date(currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
            .signWith(secretKey(properties.key))
            .compact()

    fun generateRefreshToken(memberId: String, clientId: String): String =
        Jwts.builder()
            .claim("memberId", memberId)
            .claim("clientId", clientId)
            .claim("type", "refresh")
            .issuedAt(Date(currentTimeMillis()))
            .expiration(Date(currentTimeMillis() + REFRESH_TOKEN_EXPIRE))
            .signWith(secretKey(properties.key))
            .compact()

    fun generateIdToken(clientId: String, role: RoleType, clientUrl: String, dodamId: String, key: String): String =
        Jwts.builder()
            .claim("iss", clientUrl)
            .claim("aud", clientId)
            .claim("sub", dodamId)
            .claim("role", role.name)
            .claim("exp", Date(currentTimeMillis() + properties.expire))
            .issuedAt(Date(currentTimeMillis()))
            .expiration(Date(currentTimeMillis() + properties.expire))
            .signWith(secretKey(key))
            .compact()

    fun validateToken(token: String): TokenClaims {
        val claims = parseToken(token)
        println(claims["role"])
        return TokenClaims(
            memberId = claims["memberId"] as String,
            clientId = claims["clientId"] as String,
            type = claims["type"] as String
        )
    }

    fun reissueAccessToken(refreshToken: String): String {
        val claims = validateToken(refreshToken)
        require(claims.type == "refresh") { "Invalid token type" }
        return generateAccessToken(claims.memberId, claims.clientId)
    }

    private fun parseToken(token: String): Claims =
        Jwts.parser()
            .verifyWith(secretKey(properties.key))
            .build()
            .parseSignedClaims(token)
            .payload

    private fun secretKey(key: String) =
        SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)
}

data class TokenClaims(
    val memberId: String,
    val clientId: String,
    val type: String
)
