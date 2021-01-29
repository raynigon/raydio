package io.rayd.backend.webradio

import io.rayd.backend.testhelper.ResourceWireMockInitializer
import io.rayd.backend.webradio.dto.UpdateStationResponse
import io.rayd.backend.webradio.model.WebRadioStation
import io.rayd.backend.webradio.repository.WebRadioStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
class DeleteStationSpec extends Specification {

    @LocalServerPort
    private int port

    @Value('${wiremock.resources}')
    private String imagesUrl

    @Autowired
    private WebRadioStationRepository repository

    private WebClient client
    private WebRadioStation station

    def setup() {
        client = WebClient.create("http://localhost:$port/")
        repository.deleteAll()
        station = repository.save(new WebRadioStation(
                UUID.randomUUID(),
                "1Live",
                new URL("https://wdr-edge-301f-fra-ts-cdn.cast.addradio.de/wdr/1live/live/mp3/128/stream.mp3"),
                null,
                true
        ))
    }

    def 'delete existing station'() {
        when:
        def result = deleteWebRadio(station.id.toString())

        then:
        result.statusCodeValue == 204
        result.body == null

        and:
        !repository.existsById(station.id)
    }


    def deleteWebRadio(String stationId) {
        return client.delete()
                .uri("/api/v1/webradio/${stationId}")
                .exchangeToMono({ response ->
                    if (response.rawStatusCode() == 204)
                        return response.toBodilessEntity()
                    else
                        return response.toEntity(String)
                }).block() as ResponseEntity
    }
}
