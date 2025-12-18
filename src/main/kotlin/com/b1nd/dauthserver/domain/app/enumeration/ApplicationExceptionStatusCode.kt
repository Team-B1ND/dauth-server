package com.b1nd.dauthserver.domain.app.enumeration

import com.b1nd.dauthserver.domain.common.enumeration.StatusCode

enum class ApplicationExceptionStatusCode(
    override val message: String,
    override val status: Int
): StatusCode {
    APPLICATION_NOT_FOUND("Application Not Found", 404),
    APPLICATION_KEY_NOT_MATCH("Application Key Not Match", 403),
    APPLICATION_NAME_ALEADY_EXIST("이미 존재하는 애플리케이션 이름", 409),
    INVALID_SCOPE("허용되지 않은 scope 요청", 400)
}