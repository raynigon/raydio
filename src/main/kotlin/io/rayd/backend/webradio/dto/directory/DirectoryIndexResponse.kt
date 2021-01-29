package io.rayd.backend.webradio.dto.directory

data class DirectoryIndexResponse(
    val version: Long,
    val active: Boolean,
    val bundles: List<StationBundle>
)

data class StationBundle(
    val id: String,
    val version: Long,
    val name: String,
)
