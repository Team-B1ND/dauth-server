package com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import java.util.*

abstract class BaseUUIDEntity(
    @field:Id
    @get:JvmName("getIdentifier")
    open var userUnique: UUID = UUID(0, 0)
) : Persistable<UUID> {

    override fun getId() = userUnique

    override fun isNew() = (userUnique == UUID(0, 0)).also { new ->
        if (new) userUnique = UUID.randomUUID()
    }
}