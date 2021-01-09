package io.rayd.backend.webradio.dto

import java.util.*

data class StationListResponse(
        val items: List<StationListItem>
)

data class StationListItem(
        val id: UUID,
        val name: String,
        val logo: String?
)
