package com.b1nd.dauthserver.application.app.data.response

import com.b1nd.dauthserver.application.framework.data.FrameworkResponse
import com.b1nd.dauthserver.domain.app.entity.data.ApplicationWithFrameworks
import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "어플리케이션 정보 응답")
data class ApplicationResponse(
    @Schema(description = "어플리케이션 이름", example = "My Awesome App")
    val name: String,

    @Schema(description = "어플리케이션 URL", example = "https://myapp.com")
    val url: String,

    @Schema(description = "어플리케이션 설명", example = "My Awesome App is a great app for your awesome project!")
    val description: String?,

    @Schema(description = "소유자 ID (도담 ID)", example = "legolove08")
    val ownerId: String,

    @Schema(description = "서비스 등록일", example = "2025-12-17")
    val createdAt: LocalDate,

    @Schema(description = "사용 가능한 스코프 목록", example = "[\"openid\", \"phone\", \"read:profile\"]")
    val scopes: List<ScopeType>,

    @Schema(description = "OAuth 콜백 URL (내 어플리케이션만 표시)", example = "https://myapp.com/callback")
    val redirectUrl: String?,

    @Schema(description = "OAuth 클라이언트 ID (내 어플리케이션만 표시)", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: String?,

    @Schema(description = "OAuth 클라이언트 Secret (내 어플리케이션만 표시)", example = "secret-key-12345")
    val clientSecret: String?,

    @Schema(description = "사용된 프레임워크 목록")
    val frameworks: List<FrameworkResponse>
) {
    companion object {
        fun fromEntity(application: ApplicationWithFrameworks, includeSecret: Boolean): ApplicationResponse =
            ApplicationResponse(
                name = application.application.name,
                url = application.application.url,
                ownerId = application.application.ownerId,
                createdAt = application.application.createdAt,
                scopes = application.application.scopes,
                frameworks = FrameworkResponse.of(application.frameworks),
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
