package io.rayd.backend.application

import io.rayd.backend.audio.source.MediaSource
import java.util.UUID

data class ApplicationState(
    val title: String? = null,
    val source: MediaSource? = null,
    val player: PlayerType = PlayerType.NONE,
    val tasks: Set<AppTask> = HashSet()
)

data class AppTask(
    val id: UUID,
    val name: String,
)

enum class PlayerType {
    NONE,
    WEB_RADIO,
    AIRPLAY,
    BLUETOOTH
}
