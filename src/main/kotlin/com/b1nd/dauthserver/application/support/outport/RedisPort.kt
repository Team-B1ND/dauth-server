package com.b1nd.dauthserver.application.support.outport

import reactor.core.publisher.Mono

interface RedisPort {
    fun saveData(key: String, value: Any): Mono<Void>
    fun getData(key: String): Mono<Any?>
    fun deleteData(key: String): Mono<Void>
}