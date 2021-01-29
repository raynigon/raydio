package io.rayd.backend.webradio.controller

import io.rayd.backend.webradio.dto.CreateStationRequest
import io.rayd.backend.webradio.dto.CreateStationResponse
import io.rayd.backend.webradio.dto.StationDetailsResponse
import io.rayd.backend.webradio.dto.StationListResponse
import io.rayd.backend.webradio.dto.UpdateStationRequest
import io.rayd.backend.webradio.dto.UpdateStationResponse
import io.rayd.backend.webradio.mapper.WebRadioStationMapper
import io.rayd.backend.webradio.service.WebRadioStationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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
    fun getStationList(
        @RequestParam(
            "favorites",
            defaultValue = "true"
        ) favorites: Boolean = true
    ): StationListResponse =
        (if (favorites) service.listFavorites() else service.list())
            .let { mapper.mapToListResponse(it) }
}
