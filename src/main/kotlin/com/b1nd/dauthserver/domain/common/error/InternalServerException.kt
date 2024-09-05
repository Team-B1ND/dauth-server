package com.b1nd.dauthserver.domain.common.error

object InternalServerException : BasicException(ErrorCode.INTERNAL_SERVER_ERROR.message, ErrorCode.INTERNAL_SERVER_ERROR.statusCode)
