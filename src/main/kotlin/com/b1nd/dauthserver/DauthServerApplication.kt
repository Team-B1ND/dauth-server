 package com.b1nd.dauthserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.time.ZonedDateTime

 @ConfigurationPropertiesScan
 @SpringBootApplication
class DauthServerApplication

fun main(args: Array<String>) {
    runApplication<DauthServerApplication>(*args)
}
