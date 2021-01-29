package io.rayd.backend.webradio.service

import io.rayd.backend.application.AppTask
import io.rayd.backend.application.ApplicationStateService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.UUID

interface WebRadioDirectoryService {
    fun refresh()
}

@Service
class WebRadioDirectoryServiceImpl(
    private val stateService: ApplicationStateService
) : WebRadioDirectoryService {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var running: Boolean = false

    @Async
    override fun refresh() {
        if (running) return // TODO throw already running exception here
        val task = AppTask(id = UUID.randomUUID(), name = "Refresh WebRadio Directory")
        stateService.addTask(task)
        try {
            running = true
            handleRefresh()
        } catch (exception: Throwable) {
            logger.error("WebRadio Directory refresh failed", exception)
        } finally {
            running = false
            stateService.removeTask(task.id)
        }
    }

    private fun handleRefresh() {
        TODO("Not yet implemented")
    }
}
