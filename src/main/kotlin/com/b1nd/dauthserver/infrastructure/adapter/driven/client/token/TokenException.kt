package com.b1nd.dauthserver.infrastructure.adapter.driven.client.token

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class TokenException(code: Int) : BasicException(
    message = when (code) {
        400 -> "토큰이 전송되지 않았습니다"
        401 -> "잘못된 토큰입니다."
        410 -> "만료된 토큰입니다"
        else -> ErrorCode.INTERNAL_SERVER_ERROR.message
    },
    code
)