package com.b1nd.dauthserver.application.client.exception

import com.b1nd.dauthserver.domain.common.error.BasicException
import com.b1nd.dauthserver.domain.common.error.ErrorCode

object NotFoundClientIdException : BasicException(ErrorCode.CLIENT_ID_NOT_FOUND.message, ErrorCode.CLIENT_ID_NOT_FOUND.statusCode){
}