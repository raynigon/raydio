package io.rayd.backend.webradio

import io.rayd.backend.WebRadioStation
import io.rayd.backend.audio.output.AudioPlayer
import org.springframework.web.bind.annotation.*
import java.net.URL

@RestController
@RequestMapping
class WebRadioController(
        private val audioPlayer: AudioPlayer
) {

    @PostMapping
    fun play(){
        audioPlayer.play(WebRadioStation(URL("https://wdr-edge-301f-fra-ts-cdn.cast.addradio.de/wdr/1live/live/mp3/128/stream.mp3")))
        // audioPlayer.play(WebRadioStation(URL("https://file-examples-com.github.io/uploads/2017/11/file_example_WAV_10MG.wav")))

    }

    @DeleteMapping
    fun stop(){
        audioPlayer.stop()
    }
}