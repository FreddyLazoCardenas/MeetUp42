package com.androiddev.meetup42.logging.manager

import com.androiddev.meetup42.logging.provider.LogProvider

/**
 * @author Freddy Lazo.
 */
class LogManager {

    var providers: List<LogProvider> = emptyList()

    fun sendLogToDashboard(data: String) {
        providers.forEach {
            it.trackData(data)
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: LogManager? = null

        fun getInstance(): LogManager {
            return INSTANCE ?: synchronized(this) {
                val instance = LogManager()
                INSTANCE = instance
                instance
            }
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}