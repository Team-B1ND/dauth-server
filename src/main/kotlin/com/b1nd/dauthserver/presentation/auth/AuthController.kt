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

@Tag(name = "Authentication", description = "인증 API")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val useCase: AuthUseCase
) {
    @Operation(
        summary = "ID/PW 로그인",
        description = "도담도담 계정으로 로그인합니다. 성공 시 Authorization Code가 반환됩니다."
    )
    @PostMapping("/id-login")
    suspend fun login(@Valid @RequestBody request: IdLoginRequest): ResponseData<LoginResponse> =
        useCase.idLogin(request)

    @Operation(
        summary = "QR 로그인 세션 생성",
        description = "QR 로그인을 위한 세션을 생성합니다. 반환된 code와 word를 QR 코드로 표시합니다."
    )
    @PostMapping("/qr")
    suspend fun createQr(@Valid @RequestBody request: CreateQrRequest): ResponseData<QrLoginResponse> =
        useCase.createQr(request)

    @Operation(
        summary = "QR 로그인 처리 (앱)",
        description = "앱에서 QR 코드를 스캔한 후 로그인을 처리합니다."
    )
    @PostMapping("/qr-login")
    suspend fun qrLogin(@Valid @RequestBody request: QrLoginRequest): Response =
        useCase.qrLogin(request)

    @Operation(
        summary = "앱 자체 로그인",
        description = "앱에서 직접 로그인합니다. Access Token을 사용하여 인증합니다."
    )
    @PostMapping("/app-login")
    suspend fun appLogin(@Valid @RequestBody request: AppLoginRequest): Response =
        useCase.appLogin(request)

    @Operation(
        summary = "QR 로그인 확인",
        description = "QR 로그인 완료 여부를 확인합니다. 웹에서 폴링 방식으로 호출합니다."
    )
    @PostMapping("/qr/check")
    suspend fun checkQrLogin(@Valid @RequestBody request: CheckQrRequest): ResponseData<LoginResponse> =
        useCase.checkQrLogin(request)
}