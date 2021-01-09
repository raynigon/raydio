package io.rayd.backend.testhelper

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils


@Slf4j
class ResourceWireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())
    private static WireMock wiremock


    @Override
    void initialize(final ConfigurableApplicationContext applicationContext) {
        wireMockServer.start()
        def port = wireMockServer.port()
        wiremock = WireMock.create().port(port).build()
        log.info("Bind Resources Mock to Port: {}", port)
        def resourcesUri = "wiremock.resources=http://localhost:${port}"
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, resourcesUri)
    }
}
