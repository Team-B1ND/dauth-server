package com.b1nd.dauthserver.infrastructure.adapter.driven.db

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
                val user = User.fromEntity(userEntity)
                if (user != null) Mono.just(user)
                else Mono.empty()
            }
    }


    override fun saveUser(user: User): Mono<User> {
        return userRepository.saveUser(user.toEntity())
            .map { user }
    }
}