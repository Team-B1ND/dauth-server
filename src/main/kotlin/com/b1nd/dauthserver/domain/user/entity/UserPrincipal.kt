package com.b1nd.dauthserver.domain.user.entity

import com.b1nd.dauthserver.domain.user.enumeration.RoleType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(val dodamId: String, val clientId: String): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getPassword(): String? = null

    override fun getUsername(): String = dodamId

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}