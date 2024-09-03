package com.b1nd.dauthserver.domain.user.outport

import com.b1nd.dauthserver.domain.user.model.User
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.UserEntity
import reactor.core.publisher.Mono

interface UserPort {
    fun getByDodamIdAndClientId(dodamId: String, clientId: String): Mono<User?>
    fun saveUser(user: User): Mono<User>
    fun getByDodamId(dodamId: String): Mono<User?>
}