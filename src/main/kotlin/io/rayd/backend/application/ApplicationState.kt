package io.rayd.backend.application

import io.rayd.backend.audio.source.MediaSource

data class ApplicationState(
    val player: PlayerType = PlayerType.NONE,
    val source: MediaSource? = null
)

enum class PlayerType {
    NONE,
    WEB_RADIO,
    AIRPLAY,
    BLUETOOTH
}
