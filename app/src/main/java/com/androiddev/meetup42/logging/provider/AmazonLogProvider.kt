package com.androiddev.meetup42.logging.provider

import java.util.logging.Logger

/**
 * @author Freddy Lazo.
 */
class AmazonLogProvider : LogProvider {

    private lateinit var amazonLogger: Logger

    init {
        setUp()
    }

    override fun setUp() {
        amazonLogger = Logger.getAnonymousLogger()
    }

    override fun trackData(data: String) {
        amazonLogger.info(data)
    }
}