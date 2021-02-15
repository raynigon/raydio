package io.rayd.backend.webradio.io

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.function.Consumer
import java.util.regex.Pattern

class IcyMetaDataStream(
    private val streamUrl: URL,
    private val bufferSize: Long,
    private val consumer: Consumer<String>
) : InputStream() {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val client: HttpClient = HttpClientBuilder.create().build()
    private val thread: Thread = Thread(this::run)
    private val buffer: ByteQueue = ByteQueue(bufferSize.toInt())

    private var bufferFilled: Boolean = false
    private var metadata: Boolean = false
    private var counter: Int = 0
    private var interval: Int = -1
    private lateinit var stream: InputStream

    companion object {
        const val BUFFER_16_KB = 1024 * 16
        const val BUFFER_96_KB = 1024 * 96
    }

    override fun read(): Int {
        val buffer = ByteArray(1)
        if (read(buffer, 0, 1) < 0)
            return -1
        return buffer[0].toInt() + 128
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        ensureBuffered()
        return synchronized(buffer) { buffer.read(b, off, len) }
    }

    fun start() {
        openStream()
        thread.start()
    }

    override fun close() {
        thread.interrupt()
    }

    fun run() {
        try {
            if (stream.available() <= 0)
                openStream()
            while (!thread.isInterrupted) {
                try {
                    val buf = ByteArray(1024)
                    val size = copy(buf, 0, buf.size)
                    synchronized(buffer) {
                        buffer.write(buf, 0, size)
                    }
                } catch (e: Exception) {
                    logger.error("Handle Exception in ${this.javaClass.name}", e)
                    openStream()
                }
            }
        } catch (e: Throwable) {
            logger.error("Unexpected Exception in ${this.javaClass.name}", e)
        } finally {
            stream.close()
        }
    }

    private fun openStream() {
        val request = HttpGet(streamUrl.toURI())
        request.addHeader("Icy-MetaData", "1")
        val response: HttpResponse = client.execute(request)
        counter = 0
        interval = -1
        metadata = response.containsHeader("icy-metaint")
        if (metadata)
            interval = response.getFirstHeader("icy-metaint").value.toInt()
        stream = response.entity.content
    }

    private fun ensureBuffered() {
        bufferFilled = buffer.available() > BUFFER_16_KB
        while (buffer.available() < BUFFER_96_KB && !bufferFilled) {
            Thread.sleep(10)
        }
        bufferFilled = true
    }

    private fun copy(buffer: ByteArray, offset: Int, len: Int): Int {
        if (!metadata) return stream.read(buffer, offset, len)
        val length0 = if (counter + len > interval) interval - counter else len
        val read0 = stream.read(buffer, offset, length0)
        if (read0 < 0) error("Stream if EOF")
        val offset0 = offset + read0
        counter += read0
        if (counter == interval) {
            readMetadata()
            counter = 0
        }
        if (len - read0 <= 0) return read0
        val read1 = copy(buffer, offset0, len - read0)
        return read0 + read1
    }

    private fun readMetadata() {
        val size = stream.read() * 16
        if (size < 0) throw RuntimeException("Invalid Size: $size")
        if (size > 0) {
            val metadata = ByteArray(size)
            var offset = 0
            while (offset < size)
                offset += stream.read(metadata, offset, size).let { if (it < 0) error("Stream EOF") else it }
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
