package com.b1nd.dauthserver.infrastructure.security.support

import com.b1nd.dauthserver.domain.user.entity.UserEntity
import com.b1nd.dauthserver.domain.user.entity.UserPrincipal
import com.b1nd.dauthserver.domain.user.exception.UserNotFoundException
import com.b1nd.dauthserver.domain.user.repository.UserRepository
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserAuthenticationHolder(
    private val repository: UserRepository
) {
    suspend fun current(): UserEntity {
        val userPrincipal = ReactiveSecurityContextHolder.getContext().awaitSingle().authentication.principal as UserPrincipal
        return repository.findByDodamIdAndClient(userPrincipal.dodamId, userPrincipal.clientId)?: throw UserNotFoundException()
    }
}