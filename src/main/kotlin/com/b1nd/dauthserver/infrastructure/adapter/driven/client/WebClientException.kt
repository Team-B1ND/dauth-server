package com.b1nd.dauthserver.infrastructure.adapter.driven.client

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class WebClientException(code: Int) : BasicException(
    message = when (code) {
        400 -> ErrorCode.TOKEN_NOT_PROVIDED.message
        401, 500 -> ErrorCode.INVALID_TOKEN.message
        410 -> ErrorCode.EXPIRED_TOKEN.message
        else -> ErrorCode.INTERNAL_SERVER_ERROR.message
    },
    statusCode = when (code) {
        400 -> ErrorCode.TOKEN_NOT_PROVIDED.statusCode
        401, 500 -> ErrorCode.INVALID_TOKEN.statusCode
        410 -> ErrorCode.EXPIRED_TOKEN.statusCode
        else -> ErrorCode.INTERNAL_SERVER_ERROR.statusCode
    }
)