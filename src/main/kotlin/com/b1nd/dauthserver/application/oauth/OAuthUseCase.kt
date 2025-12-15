package com.b1nd.dauthserver.application.oauth

import com.b1nd.dauthserver.application.oauth.data.StandardUserInfoResponse
import com.b1nd.dauthserver.application.oauth.data.UserInfoResponse
import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.infrastructure.client.dodam.DodamClient
import com.b1nd.dauthserver.infrastructure.security.support.UserAuthenticationHolder
import com.b1nd.dauthserver.infrastructure.security.token.core.TokenProvider
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class OAuthUseCase(
    private val dodamClient: DodamClient,
    private val tokenProvider: TokenProvider
) {
    suspend fun getUserInfo(): ResponseData<UserInfoResponse> {
        val user = UserAuthenticationHolder.current()

        val accessToken = tokenProvider.reissueAccessToken(user.refreshToken)
        val memberInfo = dodamClient.dodamMy(accessToken)

        return ResponseData.ok(
            "사용자 정보 조회 성공",
            UserInfoResponse.fromMember(memberInfo, user.scopes)
        )
    }

    suspend fun getStandardUserInfo(): StandardUserInfoResponse {
        val user = UserAuthenticationHolder.current()

        val accessToken = tokenProvider.reissueAccessToken(user.refreshToken)
        val memberInfo = dodamClient.dodamMy(accessToken)

        return StandardUserInfoResponse.fromMember(memberInfo, user.scopes)
    }
}
