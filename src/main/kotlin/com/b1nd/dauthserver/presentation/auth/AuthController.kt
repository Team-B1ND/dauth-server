package com.b1nd.dauthserver.presentation.auth

import com.b1nd.dauthserver.application.auth.AuthUseCase
import com.b1nd.dauthserver.application.auth.data.AppLoginRequest
import com.b1nd.dauthserver.application.auth.data.CheckQrRequest
import com.b1nd.dauthserver.application.auth.data.CreateQrRequest
import com.b1nd.dauthserver.application.auth.data.IdLoginRequest
import com.b1nd.dauthserver.application.auth.data.LoginResponse
import com.b1nd.dauthserver.application.auth.data.QrLoginRequest
import com.b1nd.dauthserver.application.auth.data.QrLoginResponse
import com.b1nd.dauthserver.application.support.response.Response
import com.b1nd.dauthserver.application.support.response.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val useCase: AuthUseCase
): AuthDocs {
    @PostMapping("/id-login")
    override suspend fun login(@Valid @RequestBody request: IdLoginRequest): ResponseData<LoginResponse> =
        useCase.idLogin(request)

    @PostMapping("/qr")
    override suspend fun createQr(@Valid @RequestBody request: CreateQrRequest): ResponseData<QrLoginResponse> =
        useCase.createQr(request)

    @PostMapping("/qr-login")
    override suspend fun qrLogin(@Valid @RequestBody request: QrLoginRequest): Response =
        useCase.qrLogin(request)

    @PostMapping("/app-login")
    override suspend fun appLogin(@Valid @RequestBody request: AppLoginRequest): Response =
        useCase.appLogin(request)

    @PostMapping("/qr/check")
    override suspend fun checkQrLogin(@Valid @RequestBody request: CheckQrRequest): ResponseData<LoginResponse> =
        useCase.checkQrLogin(request)
}