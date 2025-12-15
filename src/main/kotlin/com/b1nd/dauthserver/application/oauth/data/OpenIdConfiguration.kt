package com.b1nd.dauthserver.application.oauth.data

import com.fasterxml.jackson.annotation.JsonProperty

data class OpenIdConfiguration(
    val issuer: String,

    @JsonProperty("authorization_endpoint")
    val authorizationEndpoint: String,

    @JsonProperty("token_endpoint")
    val tokenEndpoint: String,

    @JsonProperty("userinfo_endpoint")
    val userinfoEndpoint: String,

    @JsonProperty("jwks_uri")
    val jwksUri: String,

    @JsonProperty("response_types_supported")
    val responseTypesSupported: List<String> = listOf("code"),

    @JsonProperty("subject_types_supported")
    val subjectTypesSupported: List<String> = listOf("public"),

    @JsonProperty("id_token_signing_alg_values_supported")
    val idTokenSigningAlgValuesSupported: List<String> = listOf("HS256"),

    @JsonProperty("scopes_supported")
    val scopesSupported: List<String> = listOf("openid", "phone", "read:profile"),

    @JsonProperty("token_endpoint_auth_methods_supported")
    val tokenEndpointAuthMethodsSupported: List<String> = listOf("client_secret_post", "client_secret_basic"),

    @JsonProperty("claims_supported")
    val claimsSupported: List<String> = listOf("sub", "name", "email", "phone", "role", "profileImage"),

    @JsonProperty("grant_types_supported")
    val grantTypesSupported: List<String> = listOf("authorization_code", "refresh_token")
)
