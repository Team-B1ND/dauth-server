package com.b1nd.dauthserver.application.app.data.response

import com.b1nd.dauthserver.domain.app.entity.data.ApplicationWithFrameworks
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "어플리케이션 정보 응답")
data class ApplicationResponse(
    @Schema(description = "어플리케이션 이름", example = "My Awesome App")
    val name: String,

    @Schema(description = "어플리케이션 URL", example = "https://myapp.com")
    val url: String,

    @Schema(description = "어플리케이션 설명", example = "My Awesome App is a great app for your awesome project!")
    val description: String?,

    @Schema(description = "OAuth 콜백 URL (내 어플리케이션만 표시)", example = "https://myapp.com/callback")
    val redirectUrl: String?,

    @Schema(description = "OAuth 클라이언트 ID (내 어플리케이션만 표시)", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: String?,

    @Schema(description = "OAuth 클라이언트 Secret (내 어플리케이션만 표시)", example = "secret-key-12345")
    val clientSecret: String?,

    @Schema(description = "사용된 프레임워크 목록", example = "[\"React\", \"Spring Boot\"]")
    val frameworks: List<String>
) {
    companion object {
        fun fromEntity(application: ApplicationWithFrameworks, includeSecret: Boolean): ApplicationResponse =
            ApplicationResponse(
                name = application.application.name,
                url = application.application.url,
                frameworks = application.frameworks.map { it.name },
                description = application.application.description,
                redirectUrl = if (includeSecret) application.application.redirectUrl else null,
                clientId = if (includeSecret) application.application.clientId else null,
                clientSecret = if (includeSecret) application.application.clientSecret else null
            )

        fun of(applications: List<ApplicationWithFrameworks>): List<ApplicationResponse> =
           applications.map { fromEntity(it, false) }

        fun ofWithSecret(applications: List<ApplicationWithFrameworks>): List<ApplicationResponse> =
            applications.map { fromEntity(it, true) }
    }
}
