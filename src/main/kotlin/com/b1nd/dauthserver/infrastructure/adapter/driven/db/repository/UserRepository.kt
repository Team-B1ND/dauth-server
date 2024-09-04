package com.b1nd.dauthserver.infrastructure.adapter.driven.db.repository

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.UserEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.util.*

interface UserRepository : R2dbcRepository<UserEntity, UUID>, CustomUserRepository {
    fun findByUserUnique(userUnique: UUID)
    fun findByDodamIdAndClientId(dodamId: String, clientId: String): Mono<UserEntity?>
    fun findByDodamId(dodamId: String): Mono<UserEntity?>
    fun findByUserUniqueAndClientId(userUnique: UUID, clientId: String): Mono<UserEntity>
}