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
    fun updateTitle(title: String)
}

@Service
class ApplicationStateServiceImpl : ApplicationStateService {

    private val listeners: MutableSet<ApplicationStateListener> = HashSet()
    private var latestState = ApplicationState()

    @Synchronized
    override fun updatePlayer(source: MediaSource?) {
        updateState(latestState.copy(source = source, player = source?.type ?: PlayerType.NONE))
    }

    @Synchronized
    override fun addTask(task: AppTask) {
        updateState(latestState.copy(tasks = latestState.tasks.toMutableSet().also { it.add(task) }))
    }

    @Synchronized
    override fun removeTask(taskId: UUID) {
        val tasks = latestState.tasks.toMutableSet()
        tasks.removeIf { it.id == taskId }
        updateState(latestState.copy(tasks = tasks))
    }

    @Synchronized
    override fun register(listener: ApplicationStateListener) {
        listeners.add(listener)
        listener.stateChanged(latestState)
    }

    @Synchronized
    override fun remove(listener: ApplicationStateListener) {
        listeners.remove(listener)
    }

    @Synchronized
    override fun updateTitle(title: String) {
        updateState(
            latestState.copy(
                title = title
            )
        )
    }

    private fun updateState(newState: ApplicationState) {
        latestState = newState
        listeners.forEach { it.stateChanged(latestState) }
    }
}

interface ApplicationStateListener {

    fun stateChanged(event: ApplicationState)
}
