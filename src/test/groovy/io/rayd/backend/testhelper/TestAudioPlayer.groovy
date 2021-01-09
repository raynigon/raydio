package io.rayd.backend.testhelper

import io.rayd.backend.audio.output.AudioPlayer
import io.rayd.backend.audio.source.MediaSource
import org.jetbrains.annotations.NotNull
import org.springframework.stereotype.Service

@Service
class TestAudioPlayer implements AudioPlayer {

    private MediaSource current = null

    @Override
    void play(@NotNull MediaSource source) {
        current = source
    }

    @Override
    void stop() {
        current = null
    }

    @Override
    MediaSource current() {
        return current
    }
}
