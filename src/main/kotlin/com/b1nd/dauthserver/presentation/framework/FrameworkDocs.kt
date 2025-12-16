package com.b1nd.dauthserver.presentation.framework

import com.b1nd.dauthserver.application.framework.data.FrameworkResponse
import com.b1nd.dauthserver.application.support.response.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Framework", description = "프레임워크 정보 API")
interface FrameworkDocs {
    @Operation(
        summary = "프레임워크 목록 조회",
        description = "애플리케이션 등록 시 사용할 수 있는 프레임워크 목록을 조회합니다."
    )
    suspend fun getFrameworks(): ResponseData<List<FrameworkResponse>>
}