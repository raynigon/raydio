package io.rayd.backend.audio.output

import io.rayd.backend.application.ApplicationStateService
import io.rayd.backend.audio.source.MediaSource
import io.rayd.backend.audio.source.MediaStreamFactory
import io.rayd.backend.audio.source.StreamFactoryNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.InputStream
import javax.sound.sampled.AudioFormat
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
            currentProcessor = createAudioProcessor(source)
            currentProcessor?.start()
            stateService.updatePlayer(source)
        } catch (e: Throwable) {
            logger.error("Unable to play Audio Stream", e)
            currentProcessor = null
            stateService.updatePlayer(null)
        }
    }

    override fun stop() {
        currentProcessor?.stop()
        currentProcessor = null
        stateService.updatePlayer(null)
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

    private fun createAudioProcessor(source: MediaSource): AudioProcessor {
        val inputAudioStream = AudioSystem.getAudioInputStream(getStream(source))
        val audioFormat = getOutFormat(inputAudioStream.format)
        val lineInfo = DataLine.Info(SourceDataLine::class.java, audioFormat)
        val line = (AudioSystem.getLine(lineInfo) as SourceDataLine?)
            ?: throw AudioLineNotFoundException(lineInfo)
        val convertedAudioStream = AudioSystem.getAudioInputStream(audioFormat, inputAudioStream)

        return AudioProcessor(convertedAudioStream, audioFormat, line, source)
    }
}
