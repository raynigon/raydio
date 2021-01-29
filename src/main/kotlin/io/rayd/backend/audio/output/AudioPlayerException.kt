package io.rayd.backend.audio.output

import javax.sound.sampled.DataLine

sealed class AudioPlayerException(message: String) :
    RuntimeException(message)

data class AudioLineNotFoundException(val lineInfo: DataLine.Info) :
    AudioPlayerException("AudioLine not found, Line Class: ${lineInfo.lineClass}")
