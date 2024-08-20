package com.b1nd.dauthserver.domain.common.error.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

object InternalServerException : BasicException(ErrorCode.INTERNAL_SERVER_ERROR.message, ErrorCode.INTERNAL_SERVER_ERROR.statusCode)