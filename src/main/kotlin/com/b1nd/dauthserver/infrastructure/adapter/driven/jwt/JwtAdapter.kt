package com.b1nd.dauthserver.infrastructure.adapter.driven.jwt

import com.b1nd.dauthserver.application.support.outport.JwtPort
import com.b1nd.dauthserver.domain.auth.model.TokenInfo
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*

@Component
class JwtAdapter(
    private val jwtProperties: JwtProperties
): JwtPort {

    private val secureRandom = SecureRandom.getInstance("NativePRNGNonBlocking")
    private final val secret = jwtProperties.secret

    override fun issue(payload: JwtClaim) =
        TokenInfo(
            accessToken = provide(payload, "accessToken"),
            refreshToken = provide(payload, "refreshToken"),
            accessExpire = jwtProperties.accessExpire,
            refreshExpire = jwtProperties.refreshExpire,
            tokenType = "bearer"
        )

    override fun getVerifyKey() = jwtProperties.verify

    private fun provide(claim: JwtClaim, subject: String) =
        Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .subject(subject)
            .claims(claim.toMap())
            .issuer(jwtProperties.issuer)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtProperties.accessExpire))
            .apply { random(secureRandom) }
            .compact()

    override fun parse(token: String): JwtPayload {
        return try {
            val claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build().parseSignedClaims(token)
            JwtPayload(
                userUnique = claims.payload["userUnique"] as String,
                dodamId = claims.payload["dodamId"] as String,
                clientId = claims.payload["clientId"] as String,
                iss = jwtProperties.issuer,
                sub = claims.payload["sub"] as String
            )
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                is IllegalArgumentException -> throw EmptyTokenException
                else -> throw InvalidTokenException
            }
        }
    }
}