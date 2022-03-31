package com.androiddev.meetup42.logging.provider

/**
 * @author Freddy Lazo.
 */
interface LogProvider {
    fun setUp()
    fun trackData(data: String)
}