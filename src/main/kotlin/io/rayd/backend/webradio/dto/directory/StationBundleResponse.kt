package io.rayd.backend.webradio.dto.directory

import java.net.URL
import java.util.UUID

data class StationBundleResponse(
    val version: Long,
    val name: String,
    val stations: List<RadioStation>
)

data class RadioStation(
    val id: UUID,
    val name: String,
    val imageUrl: URL,
    val streams: List<RadioStationStream>
)

data class RadioStationStream(
    val type: String,
    val rate: Long,
    val url: URL,
)
