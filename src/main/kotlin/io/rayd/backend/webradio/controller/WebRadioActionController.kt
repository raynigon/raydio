package io.rayd.backend.webradio.controller

import io.rayd.backend.audio.output.AudioPlayer
import io.rayd.backend.webradio.service.WebRadioStationService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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
