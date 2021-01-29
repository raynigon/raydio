package io.rayd.backend.webradio

import io.rayd.backend.testhelper.ResourceWireMockInitializer
import io.rayd.backend.webradio.dto.CreateStationRequest
import io.rayd.backend.webradio.dto.CreateStationResponse
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
class CreateStationSpec extends Specification {

    @LocalServerPort
    private int port

    @Value('${wiremock.resources}')
    private String imagesUrl

    @Autowired
    private WebRadioStationRepository repository

    private WebClient client

    def setup() {
        client = WebClient.create("http://localhost:$port/")
    }

    def 'create station without logo'() {
        given:
        def request = new CreateStationRequest(
                "1Live",
                "https://wdr-edge-301f-fra-ts-cdn.cast.addradio.de/wdr/1live/live/mp3/128/stream.mp3",
                null,
                true
        )

        when:
        def result = postWebRadio(request)

        then:
        result.statusCodeValue == 201
        result.body instanceof CreateStationResponse

        and:
        repository.existsById((result.body as CreateStationResponse).id)
        repository.getOne((result.body as CreateStationResponse).id).logo == null
    }

    def 'create station with logo from url'() {
        given:
        def request = new CreateStationRequest(
                "1Live",
                "https://wdr-edge-301f-fra-ts-cdn.cast.addradio.de/wdr/1live/live/mp3/128/stream.mp3",
                "$imagesUrl/test_radio_station_logo.png",
                true
        )

        when:
        def result = postWebRadio(request)

        then:
        result.statusCodeValue == 201
        result.body instanceof CreateStationResponse

        and:
        repository.existsById((result.body as CreateStationResponse).id)
        repository.getOne((result.body as CreateStationResponse).id).logo.startsWith("data:image/png;base64,iVB")
    }

    def 'create station with logo from base64'() {
        given:
        def request = new CreateStationRequest(
                "1Live",
                "https://wdr-edge-301f-fra-ts-cdn.cast.addradio.de/wdr/1live/live/mp3/128/stream.mp3",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAMAAADz0U65AAADAFBMVEX/////4fP/N7D/HaX/Hqb/" +
                        "Vbv/8vr/tuL/MK3/KKr/AJj/aMP/gc3/n9n/sOD/CJ3/pNv/9fv/PrL/YcD/tOH/1u//1u7/E6H/X7//Sbf/o9v/AJn/" +
                        "Tbn/kNP/gM3/ZsL/BJz/EqH/t+L/WLz/I6j/JKj/4vMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgDgNQAAAA" +
                        "PUlEQVR4Xh3KIQ7AMBQCUGgAP1Gz+9+tspmom9j/w/BCIMA/AtJ9Cm5g4EpyXLCN+UIQaa5etlTPEd1+Ch+xiAZySp67" +
                        "wwAAAABJ@RU5ErkJggg==",
                true
        )

        when:
        def result = postWebRadio(request)

        then:
        result.statusCodeValue == 201
        result.body instanceof CreateStationResponse

        and:
        repository.existsById((result.body as CreateStationResponse).id)
        repository.getOne((result.body as CreateStationResponse).id).logo == request.logo
    }

    def postWebRadio(CreateStationRequest request) {
        return client.post()
                .uri("/api/v1/webradio/")
                .body(BodyInserters.fromValue(request))
                .exchangeToMono({ response ->
                    if (response.rawStatusCode() == 201)
                        return response.toEntity(CreateStationResponse)
                    else
                        return response.toEntity(String)
                }).block() as ResponseEntity
    }
}
