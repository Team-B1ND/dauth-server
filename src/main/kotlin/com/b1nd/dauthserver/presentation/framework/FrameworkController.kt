package com.b1nd.dauthserver.presentation.framework

import com.b1nd.dauthserver.application.framework.FrameworkUseCase
import com.b1nd.dauthserver.application.framework.data.FrameworkResponse
import com.b1nd.dauthserver.application.support.response.ResponseData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/framework")
class FrameworkController(
    private val useCase: FrameworkUseCase
): FrameworkDocs {
    @GetMapping
    override suspend fun getFrameworks(): ResponseData<List<FrameworkResponse>> =
        useCase.getFrameworks()
}