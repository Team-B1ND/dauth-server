package com.b1nd.dauthserver.domain.common.error.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class NotFoundRequestHandlerException : BasicException(ErrorCode.NOT_FOUND_REQUEST_HANDLER.message, ErrorCode.NOT_FOUND_REQUEST_HANDLER.statusCode)