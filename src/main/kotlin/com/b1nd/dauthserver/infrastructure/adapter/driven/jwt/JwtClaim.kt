package com.b1nd.dauthserver.infrastructure.adapter.driven.jwt

import com.b1nd.dauthserver.domain.user.model.User

data class JwtClaim(
    val userUnique: String,
    val dodamId: String,
    val clientId: String
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userUnique" to userUnique,
            "dodamId" to dodamId,
            "clientId" to clientId
        )
    }

    companion object {
        fun of(user: User, clientId: String) =
            JwtClaim(
                userUnique = user.userUnique.toString(),
                dodamId = user.dodamId,
                clientId = clientId
            )
    }
}
