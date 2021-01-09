package io.rayd.backend.webradio.controller

import io.rayd.backend.audio.output.AudioPlayer
import io.rayd.backend.webradio.model.WebRadioStation
import io.rayd.backend.webradio.service.WebRadioStationService
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.util.*

@RestController
@RequestMapping("/api/v1/webradio/{stationId}/")
class WebRadioActionController(
        private val service: WebRadioStationService,
        private val audioPlayer: AudioPlayer
) {

    @PostMapping("play")
    fun play(@PathVariable("stationId") stationId: UUID) {
        audioPlayer.play(service.getDetails(stationId))
    }

    @PostMapping("stop")
    fun stop() {
        audioPlayer.stop()
    }
}