package io.rayd.backend.webradio.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URL

const val MEGABYTE: Long = 1024 * 1024

@ConstructorBinding
@ConfigurationProperties("raydio.webradio")
data class WebRadioProperties(
    val directory: DirectoryProperties,
    val buffer: BufferProperties = BufferProperties()
) {
    @ConstructorBinding
    data class DirectoryProperties(
        val url: URL
    )

    @ConstructorBinding
    data class BufferProperties(
        val size: Long = 12 * MEGABYTE
    )
}
