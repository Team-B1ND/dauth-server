package com.b1nd.dauthserver.domain.common.error.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class InvalidTokenException : BasicException(ErrorCode.INVALID_TOKEN.message, ErrorCode.INVALID_TOKEN.statusCode)