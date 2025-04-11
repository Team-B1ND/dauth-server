package com.b1nd.dauthserver

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@ConfigurationPropertiesScan
@SpringBootApplication
class DauthServerApplication

fun main(args: Array<String>) {
    runApplication<DauthServerApplication>(*args)
}
