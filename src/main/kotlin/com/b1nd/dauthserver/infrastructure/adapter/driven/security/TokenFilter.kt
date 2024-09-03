package com.b1nd.dauthserver.infrastructure.adapter.driven.security

import com.b1nd.dauthserver.domain.user.outport.UserPort
import com.b1nd.dauthserver.infrastructure.adapter.driven.client.token.TokenClient
import org.springframework.core.annotation.Order
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Order(-100)
@Component
class TokenFilter(
    private val tokenClient: TokenClient,
    private val userPort: UserPort
) : WebFilter {

    companion object {
        const val HEADER = "Authorization"
        const val PREFIX = "Bearer "
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = extractToken(exchange.request)

        return if (token != null) {
            createAuthentication(token)
                .flatMap { auth ->
                    chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                }
        } else {
            chain.filter(exchange)
        }
    }

    private fun extractToken(request: ServerHttpRequest): String? {
        val header = request.headers.getFirst(HEADER)
        return if (header != null && header.startsWith(PREFIX)) {
            header.substring(PREFIX.length)
        } else {
            null
        }
    }

    private fun createAuthentication(token: String): Mono<UsernamePasswordAuthenticationToken> {
        return getMemberDetails(token).map { details ->
            UsernamePasswordAuthenticationToken(details, null, details.authorities)
        }
    }

    private fun getMemberDetails(token: String): Mono<CustomUserDetails> {
        return tokenClient.validate(token)
            .flatMap { res ->
                userPort.getByDodamId(res.memberId)
                    .map { user -> CustomUserDetails(user!!) }
            }
    }
}
