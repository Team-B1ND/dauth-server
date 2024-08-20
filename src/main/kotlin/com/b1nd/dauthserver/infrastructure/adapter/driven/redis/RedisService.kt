package com.b1nd.dauthserver.infrastructure.adapter.driven.redis

import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class RedisService(
    private val redisOperations: ReactiveRedisOperations<String, Any>,
    private val redisProperties: RedisProperties
) {

    fun saveData(key: String, value: Any): Mono<Void> {
        return redisOperations.opsForValue().set(key, value)
            .flatMap { redisOperations.expire(key, Duration.ofMillis(redisProperties.ttl.toLong())) }
            .then()
    }

    fun getData(key: String): Mono<Any?> {
        return redisOperations.opsForValue().get(key)
    }

    fun deleteData(key: String): Mono<Boolean> {
        return redisOperations.opsForValue().delete(key)
    }
}