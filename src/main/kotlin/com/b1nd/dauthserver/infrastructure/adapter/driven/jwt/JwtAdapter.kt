package com.b1nd.dauthserver.infrastructure.adapter.driven.jwt

import com.b1nd.dauthserver.application.support.outport.JwtPort
import com.b1nd.dauthserver.domain.auth.model.TokenInfo
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*

@Component
class JwtAdapter(
    private val jwtProperties: JwtProperties
): JwtPort {

    private val secureRandom = SecureRandom.getInstance("NativePRNGNonBlocking")
    private final val secret = jwtProperties.secret

    override fun issue(payload: JwtPayload) =
        TokenInfo(
            accessToken = provide(payload, "accessToken"),
            refreshToken = provide(payload, "refreshToken"),
            accessExpire = jwtProperties.accessExpire,
            refreshExpire = jwtProperties.refreshExpire,
            tokenType = "bearer"
        )

    private fun provide(payload: JwtPayload, subject: String) =
        Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .subject(subject)
            .claims(payload.toMap())
            .issuer(jwtProperties.issuer)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtProperties.accessExpire))
            .apply { random(secureRandom) }
            .compact()

}