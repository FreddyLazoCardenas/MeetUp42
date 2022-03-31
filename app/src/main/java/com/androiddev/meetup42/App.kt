package com.androiddev.meetup42

import android.app.Application
import com.androiddev.meetup42.logging.manager.LogManager
import com.androiddev.meetup42.logging.provider.AmazonLogProvider
import com.androiddev.meetup42.logging.provider.FacebookLogProvider

/**
 * @author Freddy Lazo.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpLogger()
    }

    private fun setUpLogger() {
        val manager = LogManager()
        manager.providers = listOf(AmazonLogProvider(), FacebookLogProvider())
    }
}