package com.b1nd.dauthserver.application.app.data.request

import com.b1nd.dauthserver.domain.app.entity.ApplicationEntity
import com.b1nd.dauthserver.domain.app.entity.ApplicationFrameworkEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.util.UUID

@Schema(description = "애플리케이션 등록 요청")
data class CreateApplicationRequest(
    @NotBlank
    @Schema(description = "애플리케이션 이름", example = "My Awesome App")
    val name: String,

    @NotBlank
    @Schema(description = "애플리케이션 URL", example = "https://myapp.com")
    val url: String,

    @NotBlank
    @Schema(description = "OAuth 콜백 URL", example = "https://myapp.com/callback")
    val redirectUrl: String,

    @Schema(description = "공개 여부", example = "true")
    val isPublic: Boolean,

    @NotEmpty
    @Schema(description = "사용하는 프레임워크 ID 목록", example = "[1, 2, 3]")
    val frameworks: List<Long>
) {
    fun toEntity(dodamId: String) =
        ApplicationEntity(
            name = name,
            url = url,
            redirectUrl = redirectUrl,
            isPublic = isPublic,
            ownerId = dodamId,
            clientId = UUID.randomUUID().toString(),
            clientSecret = UUID.randomUUID().toString()
        )

    fun toFrameWorks(applicationId: Long): List<ApplicationFrameworkEntity> =
        frameworks.map { ApplicationFrameworkEntity(applicationId = applicationId, frameworkId = it) }
}