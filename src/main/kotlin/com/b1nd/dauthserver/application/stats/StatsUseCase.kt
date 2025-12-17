package com.b1nd.dauthserver.application.stats

import com.b1nd.dauthserver.application.stats.data.ServiceCountResponse
import com.b1nd.dauthserver.application.stats.data.UserCountResponse
import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.domain.app.repository.ApplicationRepository
import com.b1nd.dauthserver.domain.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class StatsUseCase(
    private val userRepository: UserRepository,
    private val applicationRepository: ApplicationRepository
) {
    suspend fun getUserCount(): ResponseData<UserCountResponse> {
        val count = userRepository.count()
        return ResponseData.ok("전체 사용자 수 조회 성공", UserCountResponse(count))
    }

    suspend fun getServiceCount(): ResponseData<ServiceCountResponse> {
        val count = applicationRepository.count()
        return ResponseData.ok("전체 서비스 수 조회 성공", ServiceCountResponse(count))
    }
}
