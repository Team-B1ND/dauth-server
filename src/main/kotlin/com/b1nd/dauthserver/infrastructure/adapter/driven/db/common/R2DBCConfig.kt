package com.b1nd.dauthserver.infrastructure.adapter.driven.db.common

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
@EnableR2dbcAuditing
class R2DBCConfig(
    private val connectionFactory: ConnectionFactory
) : AbstractR2dbcConfiguration() {

    @Bean
    fun initializer() = ConnectionFactoryInitializer().apply {
        setConnectionFactory(connectionFactory)
        setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
    }

    override fun connectionFactory(): ConnectionFactory = connectionFactory
    override fun getCustomConverters() = listOf(UUIDToByteArrayConverter(), ByteArrayToUUIDConverter())
}