package io.rayd.backend.audio.output

import io.rayd.backend.audio.source.MediaSource
import org.slf4j.LoggerFactory
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.SourceDataLine

class AudioProcessor(
    private val audioStream: AudioInputStream,
    private val audioFormat: AudioFormat,
    private val line: SourceDataLine,
    val source: MediaSource
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val thread = Thread {
        try {
            run()
        } catch (t: Throwable) {
            logger.error("Unable to stream", t)
        }
    }

    fun start() {
        thread.start()
    }

    @Suppress("DEPRECATION")
    fun stop() {
        thread.interrupt()
        thread.join(250)
        line.close()
        audioStream.close()
        thread.join(250)
        if (thread.isAlive) {
            thread.stop()
        }
    }

    private fun run() {
        try {
            val buffer = ByteArray(4096)
            var n = 0
            line.open(audioFormat)
            line.start()
            do {
                line.write(buffer, 0, n)
                n = audioStream.read(buffer, 0, buffer.size)
            } while (n > -1 && !Thread.interrupted())
            line.drain()
            line.stop()
            audioStream.close()
        } finally {
            line.close()
            audioStream.close()
        }
    }
}
