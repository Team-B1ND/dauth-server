package com.b1nd.dauthserver.presentation.token

import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.application.token.TokenUseCase
import com.b1nd.dauthserver.application.token.data.TokenRefreshRequest
import com.b1nd.dauthserver.application.token.data.TokenRefreshResponse
import com.b1nd.dauthserver.application.token.data.TokenRequest
import com.b1nd.dauthserver.application.token.data.TokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "OAuth Token", description = "OAuth 토큰 발급 API")
@RestController
@RequestMapping("/oauth/token")
class TokenController(
    private val useCase: TokenUseCase
) {
    @Operation(
        summary = "토큰 발급",
        description = """
            Authorization Code를 사용하여 Access Token, Refresh Token, ID Token을 발급받습니다.

            **요청 파라미터:**
            - code: 로그인 후 받은 Authorization Code
            - clientSecret: 애플리케이션의 Client Secret

            **응답:**
            - access: 리소스 접근용 Access Token
            - refresh: 토큰 갱신용 Refresh Token
            - idToken: 사용자 식별 정보가 담긴 ID Token (JWT)
        """
    )
    @PostMapping
    suspend fun issueToken(@RequestBody request: TokenRequest): ResponseData<TokenResponse> =
        useCase.issueToken(request)

    @Operation(
        summary = "토큰 재발급",
        description = """
            Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.

            **요청 파라미터:**
            - refresh: 기존에 발급받은 Refresh Token

            **응답:**
            - access: 새로 발급된 Access Token
        """
    )
    @PostMapping("/reissue")
    suspend fun reissueToken(@RequestBody request: TokenRefreshRequest): ResponseData<TokenRefreshResponse> =
        useCase.reissueToken(request)
}