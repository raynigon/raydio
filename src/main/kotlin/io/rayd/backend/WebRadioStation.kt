package io.rayd.backend

import java.net.URL

data class WebRadioStation(
        val streamUrl: URL
) : MediaSource