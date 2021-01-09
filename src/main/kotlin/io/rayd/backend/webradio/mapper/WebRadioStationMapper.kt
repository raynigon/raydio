package io.rayd.backend.webradio.mapper

import io.rayd.backend.webradio.dto.*
import io.rayd.backend.webradio.model.WebRadioStation
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
abstract class WebRadioStationMapper {

    @Mapping(source = "streamUrl", target = "stream")
    abstract fun mapToCreationResponse(station: WebRadioStation): CreateStationResponse

    @Mapping(source = "streamUrl", target = "stream")
    abstract fun mapToUpdateResponse(station: WebRadioStation): UpdateStationResponse

    @Mapping(source = "streamUrl", target = "stream")
    abstract fun mapToDetailsResponse(station: WebRadioStation): StationDetailsResponse

    abstract fun mapToListItemResponse(it: WebRadioStation): StationListItem

    fun mapToListResponse(stations: List<WebRadioStation>): StationListResponse {
        return StationListResponse(stations.map { mapToListItemResponse(it) })
    }

}