package com.b1nd.dauthserver.domain.user.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class UserNotFoundException : BasicException(ErrorCode.USER_NOT_FOUND.message, ErrorCode.USER_NOT_FOUND.statusCode)