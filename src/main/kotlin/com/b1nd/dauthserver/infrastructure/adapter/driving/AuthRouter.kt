package com.b1nd.dauthserver.infrastructure.adapter.driving

import com.b1nd.dauthserver.application.auth.usecase.AuthUseCase
import com.b1nd.dauthserver.domain.user.model.UserCredentials
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthRouter(private val authUseCase: AuthUseCase) {

    @PostMapping("/login")
    fun login(@RequestBody userCredentials: UserCredentials) =
        authUseCase.login(userCredentials)
}