package io.rayd.backend.webradio

import io.rayd.backend.testhelper.ResourceWireMockInitializer
import io.rayd.backend.webradio.dto.StationDetailsResponse
import io.rayd.backend.webradio.dto.StationListResponse
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
class ReadStationSpec extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    private WebRadioStationRepository repository

    private WebClient client
    private WebRadioStation station0
    private WebRadioStation station1

    def setup() {
        client = WebClient.create("http://localhost:$port/")
        repository.deleteAll()
        station0 = repository.save(new WebRadioStation(
                UUID.randomUUID(),
                "1Live",
                new URL("https://wdr-edge-301f-fra-ts-cdn.cast.addradio.de/wdr/1live/live/mp3/128/stream.mp3"),
                null,
                true
        ))
        station1 = repository.save(new WebRadioStation(
                UUID.randomUUID(),
                "WDR2",
                new URL("http://wdr-wdr2-rheinland.icecast.wdr.de/wdr/wdr2/rheinland/mp3/128/stream.mp3"),
                null,
                true
        ))
    }

    def 'read station details'() {
        when:
        def result = getWebRadio(station0.id.toString())

        then:
        result.statusCodeValue == 200
        result.body instanceof StationDetailsResponse
        (result.body as StationDetailsResponse).id == station0.id
        (result.body as StationDetailsResponse).name == station0.name
        (result.body as StationDetailsResponse).stream == station0.streamUrl
        (result.body as StationDetailsResponse).logo == station0.logo
    }

    def 'list stations'() {
        when:
        def result = getRadioList()

        then:
        result.statusCodeValue == 200
        result.body instanceof StationListResponse
        (result.body as StationListResponse).items.size() == 2
    }

    def getWebRadio(String stationId) {
        return client.get()
                .uri("/api/v1/webradio/${stationId}")
                .exchangeToMono({ response ->
                    if (response.rawStatusCode() == 200)
                        return response.toEntity(StationDetailsResponse)
                    else
                        return response.toEntity(String)
                }).block() as ResponseEntity
    }

    def getRadioList() {
        return client.get()
                .uri("/api/v1/webradio/")
                .exchangeToMono({ response ->
                    if (response.rawStatusCode() == 200)
                        return response.toEntity(StationListResponse)
                    else
                        return response.toEntity(String)
                }).block() as ResponseEntity
    }
}
