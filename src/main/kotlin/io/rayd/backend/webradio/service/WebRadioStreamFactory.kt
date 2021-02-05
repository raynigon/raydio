package io.rayd.backend.webradio.service

import io.rayd.backend.application.ApplicationStateService
import io.rayd.backend.audio.source.IncompatibleSourceException
import io.rayd.backend.audio.source.MediaSource
import io.rayd.backend.audio.source.MediaStreamFactory
import io.rayd.backend.webradio.configuration.WebRadioProperties
import io.rayd.backend.webradio.model.WebRadioStation
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpHead
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.function.Consumer
import java.util.regex.Pattern

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
        val icy = createIcyStream((source as WebRadioStation))
        icy?.start()
        return BufferedInputStream(source.streamUrl.openStream(), properties.buffer.size.toInt())
    }

    private fun createIcyStream(source: WebRadioStation): IcyInputStream? {
        val client: HttpClient = HttpClientBuilder.create().build()
        val request = HttpGet(source.streamUrl.toURI())
        request.addHeader("Icy-MetaData", "1")
        val response: HttpResponse = client.execute(request)
        if (!response.containsHeader("icy-metaint"))
            return null
        response.entity.content.close()
        return IcyInputStream(source.streamUrl, stateService::updateTitle)
    }
}

class IcyInputStream(
    private val streamUrl: URL,
    private val consumer: Consumer<String>
) : Thread() {

    private var stream: InputStream? = null
    private var interval: Int = 0
    private var counter: Int = 0

    override fun run() {
        while (!isInterrupted) {
            try {
                val client: HttpClient = HttpClientBuilder.create().build()
                val request = HttpGet(streamUrl.toURI())
                request.addHeader("Icy-MetaData", "1")
                val response: HttpResponse = client.execute(request)
                counter = 0
                interval = response.getFirstHeader("icy-metaint").value.toInt()
                stream = response.entity.content
                val buffer = ByteArray(1024)
                while (!isInterrupted) {
                    read(buffer, 0, buffer.size)
                }
            } catch (ex: Throwable) {
                println(ex)
            }
        }
    }

    @Throws(IOException::class)
    fun read(buffer: ByteArray, offset: Int, len: Int): Int {
        val length0 = if (counter + len > interval) interval - counter else len
        val read0 = stream!!.read(buffer, offset, length0)
        val offset0 = offset + length0
        counter += read0
        if (counter == interval) {
            readMetadata()
            counter = 0
        }
        if (len - length0 <= 0) return read0
        val read1 = read(buffer, offset0, len - length0)
        return read0 + read1
    }

    private fun readMetadata() {
        val size = stream!!.read() * 16
        if (size > 0) {
            val metadata = ByteArray(size)
            var offset = 0
            while (offset < size)
                offset += stream!!.read(metadata, offset, size)
            parseMetadata(String(metadata, StandardCharsets.UTF_8))
        }
    }

    private fun parseMetadata(data: String) {
        val match = Pattern.compile("StreamTitle='([^;]*)'").matcher(data.trim { it <= ' ' })
        if (match.find()) {
            // Presume artist/title is separated by " - ".
            val metadata = match.group(1).split(" - ").toTypedArray()
            when (metadata.size) {
                3 -> consumer.accept("${metadata[1]}, ${metadata[2]}")
                2 -> consumer.accept("${metadata[0]}, ${metadata[1]}")
            }
        }
    }
}