package com.kurtnettle.harukaze

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

enum class SnackbarType {
    INFO, ERROR, SUCCESS, WARN
}

data class SnackbarEvent(
    val message: String
)

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    suspend fun sendInfo(msg: String) {
        val nmsg = "$msg|||${SnackbarType.INFO}"
        _events.send(SnackbarEvent(nmsg))
    }

    suspend fun sendWarn(msg: String) {
        val nmsg = "$msg|||${SnackbarType.WARN}"
        _events.send(SnackbarEvent(nmsg))
    }

    suspend fun sendError(msg: String) {
        val nmsg = "$msg|||${SnackbarType.ERROR}"
        _events.send(SnackbarEvent(nmsg))
    }

    suspend fun sendSuccess(msg: String) {
        val nmsg = "$msg|||${SnackbarType.SUCCESS}"
        _events.send(SnackbarEvent(nmsg))
    }
}