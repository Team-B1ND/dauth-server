package com.b1nd.dauthserver.presentation.app

import com.b1nd.dauthserver.application.app.data.request.CreateApplicationRequest
import com.b1nd.dauthserver.application.app.data.request.UpdateApplicationRequest
import com.b1nd.dauthserver.application.app.data.request.UpdateOwnerRequest
import com.b1nd.dauthserver.application.app.data.response.ApplicationResponse
import com.b1nd.dauthserver.application.app.data.response.MyApplicationResponse
import com.b1nd.dauthserver.application.support.response.Response
import com.b1nd.dauthserver.application.support.response.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag


@Tag(name = "Application", description = "OAuth 클라이언트 애플리케이션 관리 API")
interface ApplicationDocs {
    @Operation(
        summary = "애플리케이션 등록",
        description = "새로운 OAuth 클라이언트 애플리케이션을 등록합니다. Client ID와 Client Secret이 자동 생성됩니다.",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    suspend fun create(request: CreateApplicationRequest): Response

    @Operation(
        summary = "애플리케이션 소유자 변경",
        description = "애플리케이션의 소유자를 다른 사용자로 변경합니다.",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    suspend fun updateOwner(request: UpdateOwnerRequest): Response

    @Operation(
        summary = "애플리케이션 정보 수정",
        description = "애플리케이션의 이름, URL, Redirect URL, 공개 여부를 수정합니다.",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    suspend fun updateApplication(request: UpdateApplicationRequest): Response

    @Operation(
        summary = "내 애플리케이션 조회",
        description = "현재 로그인한 사용자가 등록한 애플리케이션 목록을 조회합니다. Client Secret이 포함됩니다.",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    suspend fun getMy(): ResponseData<MyApplicationResponse>

    @Operation(
        summary = "전체 애플리케이션 조회",
        description = "공개된 모든 애플리케이션 목록을 조회합니다."
    )
    suspend fun getAll(): ResponseData<List<ApplicationResponse>>
}