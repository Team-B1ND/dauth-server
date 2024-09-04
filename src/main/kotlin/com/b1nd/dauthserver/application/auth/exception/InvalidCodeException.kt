package com.b1nd.dauthserver.application.auth.exception

import com.b1nd.dauthserver.domain.common.error.BasicException

object InvalidCodeException : BasicException("변조된 code입니다", 403)