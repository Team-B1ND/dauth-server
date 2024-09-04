package com.b1nd.dauthserver.domain.user.outport

import com.b1nd.dauthserver.domain.client.model.Client
import com.b1nd.dauthserver.domain.user.model.User
import com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam.DodamLoginResponse
import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.UserEntity
import reactor.core.publisher.Mono
import java.util.*

interface UserPort {
    fun getByDodamIdAndClientId(dodamId: String, clientId: String): Mono<User?>
    fun saveUser(user: User): Mono<User>
    fun getByDodamId(dodamId: String): Mono<User?>
    fun findOrSaveUser(client: Client, dodamId: String, dodamLoginInfo: DodamLoginResponse): Mono<User>
    fun getByUserUniqueAndClientId(userUnique: String, clientId: String): Mono<User>
}