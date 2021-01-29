package io.rayd.backend.webradio

import io.rayd.backend.testhelper.ResourceWireMockInitializer
import io.rayd.backend.webradio.dto.CreateStationRequest
import io.rayd.backend.webradio.dto.CreateStationResponse
import io.rayd.backend.webradio.dto.UpdateStationRequest
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
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

@ActiveProfiles("test")
@ContextConfiguration(initializers = [ResourceWireMockInitializer])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateStationSpec extends Specification {

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

    def 'update station name'() {
        given:
        def request = new UpdateStationRequest("Wasser", null, null, null)

        when:
        def result = patchWebRadio(station.id.toString(), request)

        then:
        result.statusCodeValue == 200
        result.body instanceof UpdateStationResponse

        and:
        repository.existsById((result.body as UpdateStationResponse).id)
        repository.getOne((result.body as UpdateStationResponse).id).name == "Wasser"
    }

    def 'update station stream'() {
        given:
        def request = new UpdateStationRequest(null, "http://test.test/wasser.mp3", null, null)

        when:
        def result = patchWebRadio(station.id.toString(), request)

        then:
        result.statusCodeValue == 200
        result.body instanceof UpdateStationResponse

        and:
        repository.existsById((result.body as UpdateStationResponse).id)
        repository.getOne((result.body as UpdateStationResponse).id).getStreamUrl() == new URL("http://test.test/wasser.mp3")
    }

    def 'update station logo with url'() {
        given:
        def request = new UpdateStationRequest(null, null, "$imagesUrl/test_radio_station_logo.png", null)

        when:
        def result = patchWebRadio(station.id.toString(), request)

        then:
        result.statusCodeValue == 200
        result.body instanceof UpdateStationResponse

        and:
        repository.existsById((result.body as UpdateStationResponse).id)
        repository.getOne((result.body as UpdateStationResponse).id).logo.startsWith("data:image/png;base64,iVB")
    }

    def 'update station logo with favorite'() {
        given:
        def request = new UpdateStationRequest(null, null, null, false)

        when:
        def result = patchWebRadio(station.id.toString(), request)

        then:
        result.statusCodeValue == 200
        result.body instanceof UpdateStationResponse

        and:
        repository.existsById((result.body as UpdateStationResponse).id)
        !repository.getOne((result.body as UpdateStationResponse).id).favorite
    }

    def patchWebRadio(String stationId, UpdateStationRequest request) {
        return client.patch()
                .uri("/api/v1/webradio/${stationId}")
                .body(BodyInserters.fromValue(request))
                .exchangeToMono({ response ->
                    if (response.rawStatusCode() == 200)
                        return response.toEntity(UpdateStationResponse)
                    else
                        return response.toEntity(String)
                }).block() as ResponseEntity
    }
}
