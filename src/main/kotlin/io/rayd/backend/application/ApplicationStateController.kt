package io.rayd.backend.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.UUID

@RestController
@RequestMapping("/api/v1/application/state/")
class ApplicationStateController(
    private val objectMapper: ObjectMapper,
    private val appStateService: ApplicationStateService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("events")
    fun applicationStateEvents(): SseEmitter {
        val emitter = ApplicationStateEmitter(objectMapper)
        register(emitter)
        return emitter.getInternalEmitter()
    }

    @ExceptionHandler(AsyncRequestTimeoutException::class)
    fun handleAsyncRequestTimeoutException(
        exception: AsyncRequestTimeoutException,
        request: WebRequest?
    ): ResponseEntity<Any> {
        logger.debug("SSE Endpoint timed out. User: ${request?.remoteUser}", exception)
        return ResponseEntity.ok(mapOf("timeout" to "true"))
    }

    private fun register(emitter: ApplicationStateEmitter) {
        appStateService.register(emitter)
        emitter.getInternalEmitter().onCompletion { appStateService.remove(emitter) }
        emitter.getInternalEmitter().onError { appStateService.remove(emitter) }
        emitter.getInternalEmitter().onTimeout { appStateService.remove(emitter) }
    }
}

class ApplicationStateEmitter(
    private val mapper: ObjectMapper,
    private val emitter: SseEmitter = SseEmitter()
) : ApplicationStateListener {

    override fun stateChanged(event: ApplicationState) {
        try {
            emitter.send(
                SseEmitter.event()
                    .id(UUID.randomUUID().toString())
                    .name("application-state-update")
                    .data(mapper.writeValueAsString(event))
            )
        } catch (ex: Exception) {
            emitter.completeWithError(ex)
        }
    }

    fun getInternalEmitter(): SseEmitter {
        return emitter
    }
}
