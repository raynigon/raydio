package io.rayd.backend.airplay

import com.github.serezhka.jap2server.AirPlayServer
import com.github.serezhka.jap2server.AirplayDataConsumer
import io.rayd.backend.audio.output.AudioPlayer
import io.rayd.backend.audio.source.MediaSource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.PipedInputStream
import java.io.PipedOutputStream
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

interface AirPlayService {

    fun start()
    fun stop()
}

@Service
class AirPlayServiceImpl(
    config: AirPlayConfiguration = AirPlayConfiguration(),
    private val audioPlayer: AudioPlayer
) : AirplayDataConsumer, AirPlayService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private var source: AirPlaySource? = null

    private val airPlayServer: AirPlayServer = AirPlayServer(
        config.serverName,
        config.airPlayPort,
        config.airTunesPort,
        this
    )

    @PostConstruct
    override fun start() {
        airPlayServer.start()
    }

    @PreDestroy
    override fun stop() {
        airPlayServer.stop()
    }

    override fun onVideo(video: ByteArray?) {
        println("Received Video")
    }

    override fun onAudio(audio: ByteArray?) {
        println("--- Received Audio (${source?.inputStream?.available()}")
        if (audio == null) return
        if (source == null) {
            val tmpSource = AirPlaySource()
            tmpSource.inputStream.connect(tmpSource.outputStream)
            tmpSource.outputStream.write(audio)
            Thread {
                try {
                    audioPlayer.play(tmpSource)
                } catch (error: Throwable) {
                    logger.error("Unable to start Stream", error)
                }
            }.start()
            source = tmpSource
        }
        source!!.outputStream.write(audio)
    }
}

data class AirPlayConfiguration(
    val serverName: String = "raydio",
    val airPlayPort: Int = 5001,
    val airTunesPort: Int = 7001
)

data class AirPlaySource(
    val inputStream: PipedInputStream = PipedInputStream(1024 * 1024),
    val outputStream: PipedOutputStream = PipedOutputStream()
) : MediaSource