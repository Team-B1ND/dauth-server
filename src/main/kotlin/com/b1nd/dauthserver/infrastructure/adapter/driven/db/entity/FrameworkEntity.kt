package com.b1nd.dauthserver.infrastructure.adapter.driven.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("framework")
class FrameworkEntity(
    idx: Long?,
    name: String,
    type: Int,
    color: String
) {
    @Id
    var idx = idx
        private set


    var name = name
        private set


    var type = type
        private set


    var color = color
        private set
}