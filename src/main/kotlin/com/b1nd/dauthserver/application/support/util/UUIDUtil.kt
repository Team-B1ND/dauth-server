    package com.b1nd.dauthserver.application.support.util

    import com.b1nd.dauthserver.domain.client.model.ClientKey
    import reactor.core.publisher.Mono
    import reactor.core.scheduler.Schedulers
    import java.util.*

    object UUIDUtil {
        private fun generateCompositeUUID(): Mono<String> {
            return Mono.fromCallable {
                val uuid1 = UUID.randomUUID().toString().replace("-", "")
                val uuid2 = UUID.randomUUID().toString().replace("-", "")
                uuid1 + uuid2
            }.subscribeOn(Schedulers.boundedElastic())
        }

        fun generateClientKey(): Mono<ClientKey> {
            return Mono.zip(generateCompositeUUID(), generateCompositeUUID())
                .map { tuple ->
                    ClientKey(tuple.t1, tuple.t2)
                }
        }
    }