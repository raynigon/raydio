package io.rayd.backend.webradio.service

import io.rayd.backend.application.AppTask
import io.rayd.backend.application.ApplicationStateService
import io.rayd.backend.webradio.configuration.WebRadioProperties
import io.rayd.backend.webradio.dto.directory.DirectoryIndexResponse
import io.rayd.backend.webradio.dto.directory.RadioStation
import io.rayd.backend.webradio.dto.directory.StationBundle
import io.rayd.backend.webradio.dto.directory.StationBundleResponse
import io.rayd.backend.webradio.model.WebRadioStation
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.util.UUID

interface WebRadioDirectoryService {
    fun refresh()
}

@Service
@EnableConfigurationProperties(WebRadioProperties::class)
class WebRadioDirectoryServiceImpl(
    private val webClient: WebClient = WebClient.create(),
    private val properties: WebRadioProperties,
    private val stateService: ApplicationStateService,
    private val stationService: WebRadioStationService,
) : WebRadioDirectoryService {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var running: Boolean = false

    @Async
    override fun refresh() {
        if (running) return // TODO throw already running exception here
        val task = AppTask(id = UUID.randomUUID(), name = "refresh-station-directory")
        stateService.addTask(task)
        try {
            running = true
            handleRefresh()
        } catch (exception: Throwable) {
            logger.error("WebRadio Directory refresh failed", exception)
        } finally {
            running = false
            stateService.removeTask(task.id)
        }
    }

    private fun handleRefresh() {
        val response = fetchIndex()
        val bundles = response.bundles
            .map { it to handleBundle(it) }
        val totalCount = bundles.count()
        val successCount = bundles
            .filter { it.second }
            .count()
        logger.info("Updated $successCount/$totalCount Bundles successfully")
    }

    private fun handleBundle(bundle: StationBundle): Boolean {
        val response = fetchBundle(bundle) ?: return false
        val bundles = response.stations
            .map { it to handleStation(it) }
        val totalCount = bundles.count()
        val successCount = bundles
            .filter { it.second }
            .count()
        logger.info("Updated $successCount/$totalCount Stations successfully")
        return true
    }

    private fun handleStation(station: RadioStation): Boolean {
        val stream = station.streams.filter { it.type == "mp3" }.maxByOrNull { it.rate } ?: return false
        try {
            stationService.save(
                WebRadioStation(
                    id = station.id,
                    name = station.name,
                    streamUrl = stream.url,
                    logo = station.imageUrl?.toString(),
                    favorite = false
                )
            )
        } catch (exception: Throwable) {
            logger.error("Unable to update Station ${station.id}", exception)
            return false
        }
        return true
    }

    private fun fetchIndex(): DirectoryIndexResponse {
        val indexUri = UriComponentsBuilder.fromUri(properties.directory.url.toURI())
            .path("index.json")
            .build().toUri()
        return webClient.get()
            .uri(indexUri)
            .exchangeToMono { response ->
                when (response.statusCode()) {
                    HttpStatus.OK -> response.bodyToMono(DirectoryIndexResponse::class.java)
                    else -> response.createException().flatMap { Mono.error(it) }
                }
            }.map { response ->
                if (!response.active) error("Index is inactive, unable to proceed")
                response
            }
            .block() ?: error("Index Response is not allowed to be null")
    }

    private fun fetchBundle(
        bundle: StationBundle
    ): StationBundleResponse? {
        val bundleUri = UriComponentsBuilder.fromUri(properties.directory.url.toURI())
            .path("bundles/${bundle.id}.json")
            .build().toUri()
        return try {
            webClient.get()
                .uri(bundleUri)
                .exchangeToMono { response ->
                    when (response.statusCode()) {
                        HttpStatus.OK -> response.bodyToMono(StationBundleResponse::class.java)
                        else -> response.createException().flatMap { Mono.error(it) }
                    }
                }
                .map { response ->
                    if (response.version != bundle.version)
                        error("Bundle Version Difference in Bundle ${bundle.name} Index Version: ${bundle.version} Response Version: ${response.version}")
                    response
                }
                .block() ?: error("Bundle Response is not allowed to be null")
        } catch (exception: Throwable) {
            logger.error("Unable to update Bundle ${bundle.id}", exception)
            null
        }
    }
}
