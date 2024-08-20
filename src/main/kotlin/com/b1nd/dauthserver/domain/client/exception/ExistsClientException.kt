package com.b1nd.dauthserver.domain.client.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

class ExistsClientException : BasicException(ErrorCode.ALREADY_EXIST_CLIENT.message, ErrorCode.ALREADY_EXIST_CLIENT.statusCode)