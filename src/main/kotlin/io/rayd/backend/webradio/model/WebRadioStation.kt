package io.rayd.backend.webradio.model

import io.rayd.backend.audio.source.MediaSource
import java.net.URL
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "webradio_station")
data class WebRadioStation(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "stream", nullable = false)
    val streamUrl: URL,

    @Column(name = "logo", nullable = true)
    val logo: String?
) : MediaSource
