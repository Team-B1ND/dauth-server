package com.b1nd.dauthserver.application.auth.exception

import com.b1nd.dauthserver.domain.common.error.BasicException

object InvalidSecretException : BasicException("변조된 secret입니다", 403)