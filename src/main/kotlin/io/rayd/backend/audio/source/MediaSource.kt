package io.rayd.backend.audio.source

import io.rayd.backend.application.PlayerType

interface MediaSource {
    val type: PlayerType
}
