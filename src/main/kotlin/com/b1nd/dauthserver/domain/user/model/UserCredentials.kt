package com.b1nd.dauthserver.domain.user.model

import com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam.DodamLoginRequest

data class UserCredentials(
    val id: String,

    val pw: String,

    val clientId: String,

    val redirectUrl: String,

    val state: String? = null
){
    fun toDodamLoginRequest() =
        DodamLoginRequest(id,pw)


}