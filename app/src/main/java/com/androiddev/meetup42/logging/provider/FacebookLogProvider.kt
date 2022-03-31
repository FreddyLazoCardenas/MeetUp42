package com.androiddev.meetup42.logging.provider

/**
 * @author Freddy Lazo.
 */
class FacebookLogProvider : LogProvider {

    private lateinit var string: String

    override fun setUp() {
       string =  "Hackerman"
    }

    override fun trackData(data: String) {
        "Unused string"
    }
}