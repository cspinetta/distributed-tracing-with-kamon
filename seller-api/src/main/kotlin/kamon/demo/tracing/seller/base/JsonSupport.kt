package kamon.demo.tracing.seller.base

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object JsonSupport {

    val apiObjectMapper =
            jacksonObjectMapper()
                    .registerModule(Jdk8Module())
                    .registerModule(JavaTimeModule())
                    .setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE)
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                            SerializationFeature.FAIL_ON_EMPTY_BEANS)
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)!!
}