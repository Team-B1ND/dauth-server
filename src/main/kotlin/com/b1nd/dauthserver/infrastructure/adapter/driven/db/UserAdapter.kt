package com.b1nd.dauthserver.infrastructure.adapter.driven.db

import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.user.exception.UserNotFoundException
import com.b1nd.dauthserver.domain.user.model.User
import com.b1nd.dauthserver.domain.user.outport.UserPort
import com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam.DodamLoginResponse
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository.UserRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

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

    override fun findOrSaveUser(client: Client, dodamId: String, dodamLoginInfo: DodamLoginResponse): Mono<User> {
        return getByDodamIdAndClientId(dodamId, client.clientKey.clientId)
            .switchIfEmpty(
                saveUser(
                    User(UUID.randomUUID(), dodamLoginInfo.member.id, client.clientKey.clientId)
                )
            )
            .map { user -> user!! }
    }

    override fun getByUserUniqueAndClientId(userUnique: String, clientId: String): Mono<User> {
        return userRepository.findByUserUniqueAndClientId(UUID.fromString(userUnique), clientId)
            .flatMap { userEntity ->
                User.fromEntity(userEntity)
            }
    }
}