package io.rayd.backend.audio.output

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

const val MEGABYTE: Long = 1024 * 1024

@ConstructorBinding
@ConfigurationProperties("raydio.player.buffer")
data class AudioPlayerProperties(
        val bufferSize: Long = 4 * MEGABYTE
)
