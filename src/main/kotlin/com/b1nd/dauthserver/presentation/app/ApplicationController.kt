package com.b1nd.dauthserver.presentation.app

import com.b1nd.dauthserver.application.app.ApplicationUseCase
import com.b1nd.dauthserver.application.app.data.request.CreateApplicationRequest
import com.b1nd.dauthserver.application.app.data.request.UpdateApplicationRequest
import com.b1nd.dauthserver.application.app.data.request.UpdateOwnerRequest
import com.b1nd.dauthserver.application.app.data.response.ApplicationResponse
import com.b1nd.dauthserver.application.app.data.response.MyApplicationResponse
import com.b1nd.dauthserver.application.support.response.Response
import com.b1nd.dauthserver.application.support.response.ResponseData
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/app")
class ApplicationController(
    private val useCase: ApplicationUseCase
): ApplicationDocs {
    @PostMapping
    override suspend fun create(@Valid @RequestBody request: CreateApplicationRequest): Response =
        useCase.create(request)


    @PatchMapping("/owner")
    override suspend fun updateOwner(@Valid @RequestBody request: UpdateOwnerRequest): Response =
        useCase.updateOwner(request)

    @PatchMapping
    override suspend fun updateApplication(@Valid @RequestBody request: UpdateApplicationRequest): Response =
        useCase.updateInfo(request)

    @GetMapping("/my")
    override suspend fun getMy(): ResponseData<MyApplicationResponse> =
        useCase.getMy()

    @GetMapping
    override suspend fun getAll(): ResponseData<List<ApplicationResponse>> =
        useCase.getAll()
}