package com.b1nd.dauthserver.infrastructure.adapter.driven.redis

import com.b1nd.dauthserver.application.support.outport.RedisPort
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class RedisAdapter(
    private val redisOperations: ReactiveRedisOperations<String, Any>,
    private val redisProperties: RedisProperties
): RedisPort {

    override fun saveData(key: String, value: Any): Mono<Void> {
        return redisOperations.opsForValue().set(key, value)
            .flatMap { redisOperations.expire(key, Duration.ofMillis(redisProperties.ttl.toLong())) }
            .then()
    }

    override fun getData(key: String): Mono<Any?> {
        return redisOperations.opsForValue().get(key)
    }

    override fun deleteData(key: String): Mono<Void> {
        return redisOperations.opsForValue().delete(key)
            .then()
    }
}