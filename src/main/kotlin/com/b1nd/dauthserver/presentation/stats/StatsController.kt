package com.b1nd.dauthserver.presentation.stats

import com.b1nd.dauthserver.application.stats.StatsUseCase
import com.b1nd.dauthserver.application.stats.data.ServiceCountResponse
import com.b1nd.dauthserver.application.stats.data.UserCountResponse
import com.b1nd.dauthserver.application.support.response.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Stats", description = "DAuth 통계 API (웹 UI용)")
@RestController
@RequestMapping("/stats")
class StatsController(
    private val statsUseCase: StatsUseCase
) {
    @Operation(summary = "전체 사용자 수", description = "DAuth에 가입한 전체 사용자 수를 반환합니다.")
    @GetMapping("/users/count")
    suspend fun getUserCount(): ResponseData<UserCountResponse> =
        statsUseCase.getUserCount()

    @Operation(summary = "전체 서비스 수", description = "DAuth에 등록된 전체 서비스(앱) 수를 반환합니다.")
    @GetMapping("/services/count")
    suspend fun getServiceCount(): ResponseData<ServiceCountResponse> =
        statsUseCase.getServiceCount()
}
