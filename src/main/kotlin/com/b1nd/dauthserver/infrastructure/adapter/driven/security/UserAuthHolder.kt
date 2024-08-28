package com.b1nd.dauthserver.infrastructure.adapter.driven.security

import com.b1nd.dauthserver.domain.user.model.User
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserAuthHolder {
    fun current(): Mono<User> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication.principal as CustomUserDetails }
            .map { it.user }
    }
}
