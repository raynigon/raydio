package io.rayd.backend.audio.source

import java.io.InputStream
import kotlin.jvm.Throws

interface MediaStreamFactory {

    fun isCompatible(source: MediaSource): Boolean

    @Throws(IncompatibleSourceException::class)
    fun openStream(source: MediaSource): InputStream
}

data class IncompatibleSourceException(val source: MediaSource) :
    RuntimeException("Incompatible Media Source: ${source.javaClass}")


data class StreamFactoryNotFoundException(val source: MediaSource) :
    RuntimeException("No Stream Factory was found for Media Source: ${source.javaClass}")