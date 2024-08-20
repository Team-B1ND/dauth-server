package com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.UserEntity
import reactor.core.publisher.Mono

interface CustomUserRepository {
    fun saveUser(userEntity: UserEntity): Mono<UserEntity>
}