package com.b1nd.dauthserver.application.oauth

import com.b1nd.dauthserver.application.oauth.data.StandardUserInfoResponse
import com.b1nd.dauthserver.application.oauth.data.UserInfoResponse
import com.b1nd.dauthserver.application.support.response.ResponseData
import com.b1nd.dauthserver.infrastructure.client.dodam.DodamClient
import com.b1nd.dauthserver.infrastructure.security.support.UserAuthenticationHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class OAuthUseCase(
    private val dodamClient: DodamClient,
    private val holder: UserAuthenticationHolder
) {
    suspend fun getUserInfo(): ResponseData<UserInfoResponse> {
        val user = holder.current()
        val accessToken = dodamClient.reissueAccessToken(user.refreshToken)
        val memberInfo = dodamClient.dodamMy(accessToken)
        val memberClubInfo = dodamClient.dodamMyClub(accessToken)
        return ResponseData.ok("사용자 정보 조회 성공", UserInfoResponse.of(memberInfo, memberClubInfo, user.scopes))
    }

    suspend fun getStandardUserInfo(): UserInfoResponse {
        val user = holder.current()
        val accessToken = dodamClient.reissueAccessToken(user.refreshToken)
        val memberInfo = dodamClient.dodamMy(accessToken)
        val memberClubInfo = dodamClient.dodamMyClub(accessToken)
        return UserInfoResponse.of(memberInfo, memberClubInfo, user.scopes)
    }
}
