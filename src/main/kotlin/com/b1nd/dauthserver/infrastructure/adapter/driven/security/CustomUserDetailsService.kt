package com.b1nd.dauthserver.infrastructure.adapter.driven.security

import com.b1nd.dauthserver.domain.user.exception.UserNotFoundException
import com.b1nd.dauthserver.domain.user.model.User
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*


@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails>? {
        return Mono.justOrEmpty(username)
            .map { UUID.fromString(it) }
            .flatMap { userId: UUID ->
                userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(UserNotFoundException()))
                    .map { user -> CustomUserDetails(User.fromEntity(user)!!) }
            }
    }
}