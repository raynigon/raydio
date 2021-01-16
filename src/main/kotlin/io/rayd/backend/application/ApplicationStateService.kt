package io.rayd.backend.application

import org.springframework.stereotype.Service

interface ApplicationStateService {
    fun register(listener: ApplicationStateListener)
    fun remove(listener: ApplicationStateListener)
    fun update(state: ApplicationState)
}

@Service
class ApplicationStateServiceImpl : ApplicationStateService {

    private val listeners: MutableSet<ApplicationStateListener> = HashSet()
    private var lastState = ApplicationState()

    override fun update(state: ApplicationState) {
        lastState = state
        listeners.forEach { it.stateChanged(lastState) }
    }

    override fun register(listener: ApplicationStateListener) {
        listeners.add(listener)
        listener.stateChanged(lastState)
    }

    override fun remove(listener: ApplicationStateListener) {
        listeners.remove(listener)
    }
}

interface ApplicationStateListener {

    fun stateChanged(event: ApplicationState)
}