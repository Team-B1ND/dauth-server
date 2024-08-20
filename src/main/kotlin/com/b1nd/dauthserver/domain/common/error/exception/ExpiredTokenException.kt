package com.b1nd.dauthserver.domain.common.error.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class ExpiredTokenException : BasicException(ErrorCode.EXPIRED_TOKEN.message, ErrorCode.EXPIRED_TOKEN.statusCode)