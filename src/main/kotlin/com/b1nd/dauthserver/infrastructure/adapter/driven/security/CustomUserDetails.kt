package com.b1nd.dauthserver.infrastructure.adapter.driven.security

import com.b1nd.dauthserver.domain.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: User
) : UserDetails{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun getPassword(): String? = null

    override fun getUsername(): String = user.userUnique.toString()
}