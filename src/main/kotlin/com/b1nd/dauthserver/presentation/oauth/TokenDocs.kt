package com.b1nd.dauthserver.presentation.oauth

import com.b1nd.dauthserver.application.oauth.data.StandardUserInfoResponse
import com.b1nd.dauthserver.application.oauth.data.UserInfoResponse
import com.b1nd.dauthserver.application.token.data.InternalTokenRequest
import com.b1nd.dauthserver.application.token.data.StandardTokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange

@Tag(name = "OAuth Token", description = "OAuth 2.0 토큰 엔드포인트")
interface TokenDocs {

    @Operation(
        summary = "토큰 발급",
        description = """
            OAuth 2.0 토큰 엔드포인트 (RFC 6749)

            **지원하는 grant_type:**
            - authorization_code: Authorization Code로 토큰 발급
            - refresh_token: Refresh Token으로 Access Token 재발급

            **인증 방식:**
            - client_secret_post: client_id, client_secret을 body에 포함
            - client_secret_basic: Authorization 헤더에 Basic 인증
        """,
        requestBody = RequestBody(
            required = true,
            content = [Content(
                mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                schema = Schema(implementation = TokenFormRequest::class)
            )]
        ),
        parameters = [
            Parameter(
                name = "Authorization",
                description = "Basic 인증 (Base64 encoded client_id:client_secret)",
                `in` = ParameterIn.HEADER,
                required = false,
                example = "Basic Y2xpZW50X2lkOmNsaWVudF9zZWNyZXQ="
            )
        ]
    )
    suspend fun token(
        exchange: ServerWebExchange,
        authorization: String?
    ): StandardTokenResponse

    @Operation(
        summary = "사용자 정보 조회",
        description = "OAuth 2.0 UserInfo 엔드포인트 (OpenID Connect)",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    suspend fun getUserInfo(): UserInfoResponse

    @Operation(
        summary = "내부 토큰 발급 (DAuth 전용)",
        description = """
            DAuth 내부 시스템 전용 토큰 발급 엔드포인트입니다.
            client_secret 없이 client_id만으로 토큰을 발급합니다.

            **용도:** DAuth 관리 페이지 등 내부 시스템에서 사용

            **지원하는 grant_type:**
            - authorization_code: Authorization Code로 토큰 발급
            - refresh_token: Refresh Token으로 Access Token 재발급
        """
    )
    suspend fun tokenInternal(request: InternalTokenRequest): StandardTokenResponse
}

@Schema(description = "OAuth 2.0 토큰 요청")
data class TokenFormRequest(
    @field:Schema(
        description = "Grant type",
        example = "authorization_code",
        required = true,
        allowableValues = ["authorization_code", "refresh_token"]
    )
    val grant_type: String,

    @field:Schema(
        description = "Authorization code (grant_type=authorization_code 시 필수)",
        example = "abc123"
    )
    val code: String? = null,

    @field:Schema(
        description = "Redirect URI",
        example = "https://example.com/callback"
    )
    val redirect_uri: String? = null,

    @field:Schema(
        description = "Client ID",
        example = "my-client-id"
    )
    val client_id: String? = null,

    @field:Schema(
        description = "Client Secret",
        example = "my-client-secret"
    )
    val client_secret: String? = null,

    @field:Schema(
        description = "Refresh token (grant_type=refresh_token 시 필수)",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    val refresh_token: String? = null,

    @field:Schema(
        description = "PKCE code verifier",
        example = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
    )
    val code_verifier: String? = null
)
