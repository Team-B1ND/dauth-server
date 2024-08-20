package com.b1nd.dauthserver.application.client.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

object UnauthorizedRedirectUrlException: BasicException(ErrorCode.UNAUTHORIZED_REDIRECT_URL.message, ErrorCode.UNAUTHORIZED_REDIRECT_URL.statusCode) {
}