package io.rayd.backend.webradio.controller

import io.rayd.backend.webradio.dto.*
import io.rayd.backend.webradio.mapper.WebRadioStationMapper
import io.rayd.backend.webradio.service.WebRadioStationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.net.URL
import java.util.*

@RestController
@RequestMapping("/api/v1/webradio/")
class WebRadioController(
        private val service: WebRadioStationService,
        private val mapper: WebRadioStationMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStation(@RequestBody request: CreateStationRequest): CreateStationResponse =
            service.create(request)
                    .let { mapper.mapToCreationResponse(it) }

    @PatchMapping("{stationId}")
    fun updateStation(
            @PathVariable stationId: UUID,
            @RequestBody request: UpdateStationRequest
    ): UpdateStationResponse {
        return service.update(stationId, request)
                .let { mapper.mapToUpdateResponse(it) }
    }

    @DeleteMapping("{stationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStation(@PathVariable stationId: UUID) {
        service.delete(stationId)
    }

    @GetMapping("{stationId}")
    fun getStationDetails(@PathVariable stationId: UUID): StationDetailsResponse =
            service.getDetails(stationId)
                    .let { mapper.mapToDetailsResponse(it) }

    @GetMapping
    fun getStationList(): StationListResponse =
            service.list()
                    .let { mapper.mapToListResponse(it) }
}