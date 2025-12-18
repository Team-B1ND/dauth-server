package com.b1nd.dauthserver.domain.app.entity

import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("applications")
data class ApplicationEntity(
    @Id
    val id: Long? = null,
    var name: String,
    @Column("description")
    var description: String?,
    @Column("owner_id")
    var ownerId: String,
    @Column("client_id")
    val clientId: String,
    @Column("client_secret")
    val clientSecret: String,
    var url: String,
    @Column("redirect_url")
    var redirectUrl: String,
    @Column("is_public")
    var isPublic: Boolean,
    @Column("scopes")
    var scopes: List<ScopeType>,
    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now()
) {
    fun updateOwner(ownerId: String) {
        this.ownerId = ownerId
    }

    fun updateInfo(name: String?, url: String?, description: String?, redirectUrl: String?, isPublic: Boolean?, scopes: List<ScopeType>?) {
        if (name != null) this.name = name
        if (description != null) this.description = description
        if (url != null) this.url = url
        if (redirectUrl != null) this.redirectUrl = redirectUrl
        if (isPublic != null) this.isPublic = isPublic
        if (scopes != null) this.scopes = scopes
    }
}