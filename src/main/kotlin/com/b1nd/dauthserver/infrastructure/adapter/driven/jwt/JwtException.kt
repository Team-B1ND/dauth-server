package com.b1nd.dauthserver.infrastructure.adapter.driven.jwt

import com.b1nd.dauthserver.domain.common.error.BasicException

object EmptyTokenException : BasicException(
    "토큰이 전송되지 않았습니다", 400
)

object InvalidTokenException : BasicException(
    "위조된 토큰입니다",401
)

object ExpiredTokenException : BasicException(
    "만료된 토큰입니다",401
)

