package com.b1nd.dauthserver.infrastructure.adapter.driven.db

import com.b1nd.dauthserver.domain.user.exception.UserNotFoundException
import com.b1nd.dauthserver.domain.user.model.User
import com.b1nd.dauthserver.domain.user.outport.UserPort
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository.UserRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserAdapter(
    private val userRepository: UserRepository
): UserPort {
    override fun getByDodamIdAndClientId(dodamId: String, clientId: String): Mono<User?> {
        return userRepository.findByDodamIdAndClientId(dodamId, clientId)
            .flatMap { userEntity ->
                User.fromEntity(userEntity)
            }
    }


    override fun saveUser(user: User): Mono<User> {
        return userRepository.saveUser(user.toEntity())
            .map { user }
    }

    override fun getByDodamId(dodamId: String): Mono<User?> {
        return userRepository.findByDodamId(dodamId)
            .switchIfEmpty(Mono.error(UserNotFoundException()))
            .flatMap { userEntity ->
                User.fromEntity(userEntity)
            }
    }
}