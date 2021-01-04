package io.rayd.backend.audio.output

import io.rayd.backend.MediaSource
import io.rayd.backend.WebRadioStation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedInputStream
import java.io.InputStream
import javax.sound.sampled.*

@Service
class AudioPlayer {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var currentProcessor: AudioProcessor? = null

    fun play(source: MediaSource) {
        currentProcessor?.stop()
        try {
            val inputAudioStream = AudioSystem.getAudioInputStream(getStream(source))
                    ?: return // TODO throw an error
            val audioFormat = getOutFormat(inputAudioStream.format)
            val lineInfo = DataLine.Info(SourceDataLine::class.java, audioFormat)
            val line = (AudioSystem.getLine(lineInfo) as SourceDataLine?)
                    ?: throw RuntimeException("AudioLine not found")
            val convertedAudioStream = AudioSystem.getAudioInputStream(audioFormat, inputAudioStream)
            currentProcessor = AudioProcessor(convertedAudioStream, audioFormat, line)
            currentProcessor?.start()
        } catch (e: Throwable) {
            logger.error("Unable to start Stream", e)
        }
    }

    private fun getStream(source: MediaSource): InputStream {
        val stream = (source as WebRadioStation).streamUrl.openStream()
        return BufferedInputStream(stream, 4 * 1024 * 1024) // 4 MB Buffer
    }

    private fun getOutFormat(inFormat: AudioFormat): AudioFormat {
        val ch = inFormat.channels
        val rate = inFormat.sampleRate
        return AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false)
    }

    fun stop() {
        currentProcessor?.stop()
        currentProcessor = null
    }

}

class AudioProcessor(
        private val audioStream: AudioInputStream,
        private val audioFormat: AudioFormat,
        private val line: SourceDataLine
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
        if (thread.isAlive){
            thread.stop()
        }
        line.close()
        audioStream.close()
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