package com.b1nd.dauthserver.domain.common.error.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class UnAuthorizedException : BasicException(ErrorCode.UNAUTHORIZED.message, ErrorCode.UNAUTHORIZED.statusCode)