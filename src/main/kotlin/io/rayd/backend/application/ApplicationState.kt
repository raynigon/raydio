package io.rayd.backend.application

import io.rayd.backend.audio.source.MediaSource

data class ApplicationState(
    val source: MediaSource? = null,
    val player: PlayerType = source?.type ?: PlayerType.NONE,
)

enum class PlayerType {
    NONE,
    WEB_RADIO,
    AIRPLAY,
    BLUETOOTH
}
