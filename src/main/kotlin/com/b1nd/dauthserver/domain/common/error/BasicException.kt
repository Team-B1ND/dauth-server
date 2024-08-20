package com.b1nd.dauthserver.domain.common.error

open class BasicException(
    override val message: String,
    override val statusCode: Int
) : RuntimeException(message), ExceptionAttribute {
    override fun fillInStackTrace(): Throwable = this
}