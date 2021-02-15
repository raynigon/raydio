package io.rayd.backend.webradio.service

import io.rayd.backend.application.ApplicationStateService
import io.rayd.backend.audio.source.IncompatibleSourceException
import io.rayd.backend.audio.source.MediaSource
import io.rayd.backend.audio.source.MediaStreamFactory
import io.rayd.backend.webradio.configuration.WebRadioProperties
import io.rayd.backend.webradio.io.IcyMetaDataStream
import io.rayd.backend.webradio.model.WebRadioStation
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.io.InputStream

interface WebRadioStreamFactory : MediaStreamFactory

@Service
@EnableConfigurationProperties(WebRadioProperties::class)
class WebRadioStreamFactoryImpl(
    private val stateService: ApplicationStateService,
    private val properties: WebRadioProperties
) : WebRadioStreamFactory {

    override fun isCompatible(source: MediaSource): Boolean {
        return source is WebRadioStation
    }

    override fun openStream(source: MediaSource): InputStream {
        if (!isCompatible(source)) throw IncompatibleSourceException(source)
        return IcyMetaDataStream(
            (source as WebRadioStation).streamUrl,
            properties.buffer.size,
            stateService::updateTitle
        ).also { it.start() }
    }
}
