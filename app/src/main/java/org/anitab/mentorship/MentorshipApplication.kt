package org.anitab.mentorship

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * The entry point, a class that represents Mentorship application.
 */

class MentorshipApplication : Application() {

    companion object {

        lateinit var instance: MentorshipApplication

        /**
         * @return the instance of the Application
         */
        fun getApplication(): MentorshipApplication {
            return instance
        }

        /**
         * @return the context of the Application
         */
        fun getContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}
