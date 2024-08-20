package com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("user")
class UserEntity(
    userUnique: UUID = UUID(0, 0),
    dodamId: String,
    clientId: String
) {
    @Id
    var userUnique = userUnique
        private set
    var dodamId = dodamId
        private set
    var clientId = clientId
        private set
}