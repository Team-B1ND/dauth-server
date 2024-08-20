package com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.UserEntity
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class CustomUserRepositoryImpl(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
): CustomUserRepository {
    override fun saveUser(userEntity: UserEntity): Mono<UserEntity> {
        return r2dbcEntityTemplate.insert(UserEntity::class.java)
            .using(userEntity)
    }
}