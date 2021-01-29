package io.rayd.backend.webradio.controller

import io.rayd.backend.webradio.service.WebRadioDirectoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/webradio/directory")
class WebRadioDirectoryController(
    private val service: WebRadioDirectoryService
) {

    @PostMapping("refresh")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun refreshDirectory() {
        service.refresh()
    }
}
