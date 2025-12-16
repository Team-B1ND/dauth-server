package com.b1nd.dauthserver.presentation.token

import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.application.token.data.TokenRefreshRequest
import com.b1nd.dauthserver.application.token.data.TokenRefreshResponse
import com.b1nd.dauthserver.application.token.data.TokenRequest
import com.b1nd.dauthserver.application.token.data.TokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag


@Tag(name = "OAuth Token", description = "OAuth 토큰 발급 API")
interface TokenDocs {
    @Operation(
        summary = "토큰 발급",
        description = "Authorization Code를 사용하여 Access Token, Refresh Token, ID Token을 발급받습니다."
    )
    suspend fun issueToken(request: TokenRequest): ResponseData<TokenResponse>

    @Operation(
        summary = "토큰 재발급",
        description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다."
    )
    suspend fun reissueToken(request: TokenRefreshRequest): ResponseData<TokenRefreshResponse>
}