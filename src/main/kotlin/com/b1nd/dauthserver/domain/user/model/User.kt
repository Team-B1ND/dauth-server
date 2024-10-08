package com.b1nd.dauthserver.domain.user.model

import com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity.UserEntity
import reactor.core.publisher.Mono
import java.util.UUID

data class User(
    val userUnique: UUID,
    val dodamId: String,
    val clientId: String
){
    companion object {
        fun fromEntity(userEntity: UserEntity?): Mono<User?> {
            return Mono.fromCallable {
                userEntity?.let {
                    User(
                        userUnique = it.userUnique,
                        dodamId = it.dodamId,
                        clientId = it.clientId
                    )
                }
            }
        }

    }
    fun toEntity() =
        UserEntity(
            userUnique,
            dodamId,
            clientId
        )
}