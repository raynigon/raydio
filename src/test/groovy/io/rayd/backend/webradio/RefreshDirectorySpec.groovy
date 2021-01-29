package io.rayd.backend.webradio

import io.rayd.backend.testhelper.DirectoryWireMockInitializer
import io.rayd.backend.webradio.dto.UpdateStationRequest
import io.rayd.backend.webradio.dto.UpdateStationResponse
import io.rayd.backend.webradio.model.WebRadioStation
import io.rayd.backend.webradio.repository.WebRadioStationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

@ActiveProfiles("test")
@ContextConfiguration(initializers = [DirectoryWireMockInitializer])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RefreshDirectorySpec extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    private WebRadioStationRepository repository

    private WebClient client
    private WebRadioStation station

    def setup() {
        client = WebClient.create("http://localhost:$port/")
        repository.deleteAll()
    }

    def 'refresh station directory'() {
        given:
        true

        when:
        def response = client.post()
                .uri("/api/v1/webradio/directory/refresh")
                .retrieve()
                .toBodilessEntity()
                .block()

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        and:
        waitUntil { repository.count() == 1 }
    }

    static boolean waitUntil(Closure<Boolean> closure) {
        def step = 1
        while (step < 8) { // max 5 seconds wait
            if (closure.call()) return true
            sleep(step * step * 100)
            step++
        }
        return false
    }
}
