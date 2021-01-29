package io.rayd.backend.application

import io.rayd.backend.audio.source.MediaSource
import org.springframework.stereotype.Service
import java.util.UUID

interface ApplicationStateService {
    fun register(listener: ApplicationStateListener)
    fun remove(listener: ApplicationStateListener)
    fun updatePlayer(source: MediaSource?)
    fun addTask(task: AppTask)
    fun removeTask(taskId: UUID)
}

@Service
class ApplicationStateServiceImpl : ApplicationStateService {

    private val listeners: MutableSet<ApplicationStateListener> = HashSet()
    private var latestState = ApplicationState()

    override fun updatePlayer(source: MediaSource?) {
        updateState(latestState.copy(source = source, player = source?.type ?: PlayerType.NONE))
    }

    override fun addTask(task: AppTask) {
        updateState(latestState.copy(tasks = latestState.tasks.toMutableSet().also { it.add(task) }))
    }

    override fun removeTask(taskId: UUID) {
        val tasks = latestState.tasks.toMutableSet()
        tasks.removeIf { it.id == taskId }
        updateState(latestState.copy(tasks = tasks))
    }

    override fun register(listener: ApplicationStateListener) {
        listeners.add(listener)
        listener.stateChanged(latestState)
    }

    override fun remove(listener: ApplicationStateListener) {
        listeners.remove(listener)
    }

    private fun updateState(newState: ApplicationState) {
        latestState = newState
        listeners.forEach { it.stateChanged(latestState) }
    }
}

interface ApplicationStateListener {

    fun stateChanged(event: ApplicationState)
}
