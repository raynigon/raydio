package io.rayd.backend.webradio

import io.rayd.backend.audio.output.AudioPlayer
import io.rayd.backend.testhelper.ResourceWireMockInitializer
import io.rayd.backend.webradio.model.WebRadioStation
import io.rayd.backend.webradio.repository.WebRadioStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

@ActiveProfiles("test")
@ContextConfiguration(initializers = [ResourceWireMockInitializer])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayStationSpec extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    private WebRadioStationRepository repository

    @Autowired
    private AudioPlayer player

    private WebClient client
    private WebRadioStation station

    def setup() {
        client = WebClient.create("http://localhost:$port/")
        repository.deleteAll()
        player.stop()
        station = repository.save(new WebRadioStation(
                UUID.randomUUID(),
                "1Live",
                new URL("https://wdr-edge-301f-fra-ts-cdn.cast.addradio.de/wdr/1live/live/mp3/128/stream.mp3"),
                null,
                true
        ))
    }

    def 'play radio station'() {
        when:
        def result = playWebRadio(station.id.toString())

        then:
        result != null
        result.statusCodeValue == 200

        and:
        player.current() == station
    }


    def 'stop radio station'() {
        given:
        player.play(station)

        when:
        def result = stopWebRadio(station.id.toString())

        then:
        result != null
        result.statusCodeValue == 200

        and:
        player.current() == null
    }

    def playWebRadio(String stationId) {
        return client.post()
                .uri("/api/v1/webradio/${stationId}/play")
                .exchangeToMono({ response ->
                    if (response.rawStatusCode() == 200)
                        return response.toBodilessEntity()
                    else
                        return response.toEntity(String)
                }).block() as ResponseEntity
    }

    def stopWebRadio(String stationId) {
        return client.post()
                .uri("/api/v1/webradio/${stationId}/stop")
                .exchangeToMono({ response ->
                    if (response.rawStatusCode() == 200)
                        return response.toBodilessEntity()
                    else
                        return response.toEntity(String)
                }).block() as ResponseEntity
    }
}
