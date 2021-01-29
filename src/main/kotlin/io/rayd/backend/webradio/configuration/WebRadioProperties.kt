package io.rayd.backend.webradio.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

const val MEGABYTE: Long = 1024 * 1024

@ConstructorBinding
@ConfigurationProperties("raydio.webradio")
data class WebRadioProperties(
    val buffer: BufferProperties = BufferProperties()
)

@ConstructorBinding
data class BufferProperties(
    val size: Long = 4 * MEGABYTE
)