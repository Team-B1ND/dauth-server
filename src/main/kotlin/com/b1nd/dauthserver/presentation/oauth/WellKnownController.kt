package com.b1nd.dauthserver.presentation.oauth

import com.b1nd.dauthserver.application.oauth.data.OpenIdConfiguration
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "OpenID Connect Discovery", description = "OpenID Connect Discovery 엔드포인트")
@RestController
class WellKnownController(
    @Value("\${app.oauth.issuer:https://dauth.b1nd.com}") private val issuer: String
) {
    @Operation(
        summary = "OpenID Configuration",
        description = "OpenID Connect Discovery 엔드포인트. Spring Security OAuth2 Client가 자동으로 호출합니다."
    )
    @GetMapping("/.well-known/openid-configuration")
    fun getOpenIdConfiguration(): OpenIdConfiguration {
        return OpenIdConfiguration(
            issuer = issuer,
            authorizationEndpoint = "$issuer/oauth/authorize",
            tokenEndpoint = "$issuer/oauth/token",
            userinfoEndpoint = "$issuer/oauth/userinfo",
            jwksUri = "$issuer/oauth/jwks"
        )
    }
}
