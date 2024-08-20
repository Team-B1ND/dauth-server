package com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("front_end")
class FrontEndEntity (
    idx: Long?,
    clientId: String,
    frameworkId:String
) {
    @Id
    var idx = idx
        private set

    var clientId = clientId
        private set

    var frameworkId = frameworkId
        private set
}