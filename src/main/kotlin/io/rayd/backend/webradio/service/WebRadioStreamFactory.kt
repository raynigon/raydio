package io.rayd.backend.webradio.service

import io.rayd.backend.audio.source.IncompatibleSourceException
import io.rayd.backend.audio.source.MediaSource
import io.rayd.backend.audio.source.MediaStreamFactory
import io.rayd.backend.webradio.configuration.WebRadioProperties
import io.rayd.backend.webradio.model.WebRadioStation
import org.springframework.stereotype.Service
import java.io.BufferedInputStream
import java.io.InputStream

interface WebRadioStreamFactory : MediaStreamFactory

@Service
class WebRadioStreamFactoryImpl(
    private val properties: WebRadioProperties
) : WebRadioStreamFactory {
    override fun isCompatible(source: MediaSource): Boolean {
        return source is WebRadioStation
    }

    override fun openStream(source: MediaSource): InputStream {
        if (!isCompatible(source)) throw IncompatibleSourceException(source)
        val stream = (source as WebRadioStation).streamUrl.openStream()
        return BufferedInputStream(stream, properties.buffer.size.toInt())
    }
}
