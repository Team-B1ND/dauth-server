package com.b1nd.dauthserver.presentation.oauth

import com.b1nd.dauthserver.application.oauth.OAuthUseCase
import com.b1nd.dauthserver.application.oauth.data.UserInfoResponse
import com.b1nd.dauthserver.application.support.response.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "OAuth", description = "OAuth 2.0 API")
@RestController
@RequestMapping("/oauth")
class OAuthController(
    private val useCase: OAuthUseCase
) {
    @Operation(
        summary = "사용자 정보 조회",
        description = "Access Token을 이용하여 사용자 정보를 조회합니다. 응답 데이터는 승인된 scope에 따라 달라집니다.",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @GetMapping("/userinfo")
    suspend fun getUserInfo(): ResponseData<UserInfoResponse> =
        useCase.getUserInfo()
}
