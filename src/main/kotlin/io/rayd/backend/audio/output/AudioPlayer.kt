package io.rayd.backend.audio.output

import io.rayd.backend.application.ApplicationState
import io.rayd.backend.application.ApplicationStateService
import io.rayd.backend.audio.source.MediaSource
import io.rayd.backend.audio.source.MediaStreamFactory
import io.rayd.backend.audio.source.StreamFactoryNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.InputStream
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

interface AudioPlayer {
    fun play(source: MediaSource)
    fun stop()
    fun current(): MediaSource?
}

@Service
@Profile("!test")
class DefaultAudioPlayer(
    private val stateService: ApplicationStateService,
    private val streamFactories: List<MediaStreamFactory>
) : AudioPlayer {
    private val logger = LoggerFactory.getLogger(javaClass)

    // Ensure there is only one current processor
    companion object {
        private var currentProcessor: AudioProcessor? = null
    }

    override fun play(source: MediaSource) {
        currentProcessor?.stop()
        try {
            val inputAudioStream = AudioSystem.getAudioInputStream(getStream(source))
            val audioFormat = getOutFormat(inputAudioStream.format)
            val lineInfo = DataLine.Info(SourceDataLine::class.java, audioFormat)
            val line = (AudioSystem.getLine(lineInfo) as SourceDataLine?)
                ?: throw AudioLineNotFoundException(lineInfo)
            val convertedAudioStream = AudioSystem.getAudioInputStream(audioFormat, inputAudioStream)

            // Start Audio Processor
            currentProcessor = AudioProcessor(convertedAudioStream, audioFormat, line, source)
            currentProcessor?.start()
            stateService.update(ApplicationState(source))
        } catch (e: Throwable) {
            logger.error("Unable to start Stream", e)
        }
    }

    override fun stop() {
        currentProcessor?.stop()
        currentProcessor = null
        stateService.update(ApplicationState())
    }

    override fun current(): MediaSource? {
        return currentProcessor?.source
    }

    private fun getStream(source: MediaSource): InputStream {
        return streamFactories
            .filter { it.isCompatible(source) }
            .take(1)
            .map { it.openStream(source) }
            .firstOrNull() ?: throw StreamFactoryNotFoundException(source)
    }

    private fun getOutFormat(inFormat: AudioFormat): AudioFormat {
        val ch = inFormat.channels
        val rate = inFormat.sampleRate
        return AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false)
    }
}

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

sealed class AudioPlayerException(message: String) :
    RuntimeException(message)

data class AudioLineNotFoundException(val lineInfo: DataLine.Info) :
    AudioPlayerException("AudioLine not found, Line Class: ${lineInfo.lineClass}")
