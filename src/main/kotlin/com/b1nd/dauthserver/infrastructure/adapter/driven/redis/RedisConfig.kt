package com.b1nd.dauthserver.infrastructure.adapter.driven.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableAutoConfiguration(exclude = [RedisAutoConfiguration::class, RedisReactiveAutoConfiguration::class])
class RedisConfig(
    private val redisProperties: RedisProperties,
) {

    @Bean
    fun redisConnectionFactory(): ReactiveRedisConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration().apply {
            hostName = redisProperties.host
            port = redisProperties.port
        })
    }

    @Bean
    fun redisOperations(
        factory: ReactiveRedisConnectionFactory,
        objectMapper: ObjectMapper
    ): ReactiveRedisOperations<String, Any> {
        val stringSerializer = StringRedisSerializer()
        val serializationContext = RedisSerializationContext.newSerializationContext<String, Any>()
            .key(stringSerializer)
            .value(GenericJackson2JsonRedisSerializer(objectMapper))
            .hashKey(stringSerializer)
            .hashValue(stringSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}
