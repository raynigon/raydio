package io.rayd.backend.testhelper

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import groovy.util.logging.Slf4j
import org.eclipse.jetty.http.HttpHeader
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.support.TestPropertySourceUtils

import static com.github.tomakehurst.wiremock.client.WireMock.*


@Slf4j
class DirectoryWireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
    private static WireMock wiremock


    @Override
    void initialize(final ConfigurableApplicationContext applicationContext) {
        wireMockServer.start()
        def port = wireMockServer.port()
        wiremock = create().port(port).build()
        log.info("Bind Station Directory Mock to Port: {}", port)
        def directoryUri = "raydio.webradio.directory.url=http://localhost:${port}/v1/"
        def resourceUri = "wiremock.resources=http://localhost:${port}"
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, directoryUri, resourceUri)

        wiremock.register(
                get("/v1/index.json")
                        .willReturn(
                                aResponse()
                                        .withHeader(HttpHeader.CONTENT_TYPE.lowerCaseName(), MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(
                                                """
{
    "version": 1, 
    "active": true, 
    "bundles": [
        {
            "id": "europe-1",
            "name": "Europe",
            "version": 1
        }
    ]
}""")
                        )
        )
        wiremock.register(
                get("/v1/bundles/europe-1.json")
                        .willReturn(
                                aResponse()
                                        .withHeader(HttpHeader.CONTENT_TYPE.lowerCaseName(), MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(
                                                """
{
    "version": 1, 
    "name": "Europe", 
    "stations": [
        {
            "id": "e17fcb79-967f-40b4-9c12-b0d4054ea58f",
            "name": "1Live",
            "imageUrl": "http://localhost:${port}/test_radio_station_logo.png",
            "streams": [
                {
                    "type": "mp3",
                    "rate": 128,
                    "url": "http://wdr-1live-live.icecast.wdr.de/wdr/1live/live/mp3/128/stream.mp3"
                }
            ]
        }
    ]
}""")
                        )
        )
    }
}
