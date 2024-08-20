package com.b1nd.dauthserver.domain.common.error

interface ExceptionAttribute {
    val message: String
    val statusCode: Int
}