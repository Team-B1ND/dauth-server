package com.b1nd.dauthserver.infrastructure.adapter.driven.client.dodam

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class DodamClientException (code: Int) : BasicException(
    message = when (code) {
        401 -> ErrorCode.INVALID_TOKEN.message
        403 -> ErrorCode.INVALID_PASSWORD.message
        404 -> ErrorCode.USER_NOT_FOUND.message
        else -> ErrorCode.INTERNAL_SERVER_ERROR.message
    },
    statusCode = when (code) {
        401 -> ErrorCode.INVALID_TOKEN.statusCode
        403 -> ErrorCode.INVALID_PASSWORD.statusCode
        404 -> ErrorCode.USER_NOT_FOUND.statusCode
        else -> ErrorCode.INTERNAL_SERVER_ERROR.statusCode
    }
)