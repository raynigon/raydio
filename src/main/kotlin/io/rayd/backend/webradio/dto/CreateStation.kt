package io.rayd.backend.webradio.dto

import java.net.URL
import java.util.UUID

data class CreateStationRequest(
    val name: String,
    val stream: String,
    val logo: String?,
    val favorite: Boolean,
)

data class CreateStationResponse(
    val id: UUID,
    val name: String,
    val stream: URL,
    val logo: String?,
    val favorite: Boolean,
)
