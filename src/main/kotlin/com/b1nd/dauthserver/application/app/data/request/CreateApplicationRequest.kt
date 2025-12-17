package com.b1nd.dauthserver.application.app.data.request

import com.b1nd.dauthserver.domain.app.entity.ApplicationEntity
import com.b1nd.dauthserver.domain.app.entity.ApplicationFrameworkEntity
import com.b1nd.dauthserver.domain.user.enumeration.ScopeType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.util.UUID

@Schema(description = "어플리케이션 등록 요청")
data class CreateApplicationRequest(
    @NotBlank
    @Schema(description = "어플리케이션 이름", example = "My Awesome App")
    val name: String,

    @Nullable
    @Schema(description = "어플리케이션 설명", example = "My Awesome App is a great app for your awesome project!")
    val description: String? = null,

    @NotBlank
    @Schema(description = "어플리케이션 URL", example = "https://myapp.com")
    val url: String,

    @NotBlank
    @Schema(description = "OAuth 콜백 URL", example = "https://myapp.com/callback")
    val redirectUrl: String,

    @Schema(description = "공개 여부", example = "true")
    val isPublic: Boolean,

    @Schema(description = "사용하는 프레임워크 ID 목록", example = "[1, 2, 3]")
    val frameworks: List<Long>,

    @NotEmpty
    @Schema(description = "사용할 스코프 목록", example = "[\"openid\", \"phone\", \"read:profile\"]")
    val scopes: List<ScopeType>
) {
    fun toEntity(dodamId: String) =
        ApplicationEntity(
            name = name,
            url = url,
            redirectUrl = redirectUrl,
            isPublic = isPublic,
            ownerId = dodamId,
            description = description,
            clientId = UUID.randomUUID().toString(),
            clientSecret = UUID.randomUUID().toString(),
            scopes = scopes
        )

    fun toFrameWorks(applicationId: Long): List<ApplicationFrameworkEntity> =
        frameworks.map { ApplicationFrameworkEntity(applicationId = applicationId, frameworkId = it) }
}