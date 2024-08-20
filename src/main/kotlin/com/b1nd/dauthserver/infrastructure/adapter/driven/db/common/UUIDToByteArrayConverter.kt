package com.b1nd.dauthserver.infrastructure.adapter.driven.db.common

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import java.nio.ByteBuffer
import java.util.UUID

@WritingConverter
class UUIDToByteArrayConverter : Converter<UUID, ByteArray> {

    override fun convert(source: UUID): ByteArray =
        ByteBuffer.wrap(ByteArray(16)).apply {
            putLong(source.mostSignificantBits)
            putLong(source.leastSignificantBits)
        }.array()
}