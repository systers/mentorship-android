package org.anitab.mentorship.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.anitab.mentorship.R
import org.anitab.mentorship.utils.PreferenceManager

/**
 * This activity will show the organisation logo for sometime and then start the next activity
 */
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var SPLASH_DISPLAY_LENGTH: Long = 1000
    private val preferenceManager: PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val preferences =
            getSharedPreferences(getString(R.string.intro_prefs), Context.MODE_PRIVATE)
        val firstRun = preferences.getBoolean(getString(R.string.intro_prefs_first_run), true)

        if (firstRun) {
            startActivity(Intent(this, IntroActivity::class.java))
        } else {
            val intent = if (preferenceManager.authToken.isEmpty()) {
                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }

        runnable = Runnable {
            startActivity(intent)
            finish()
        }
        handler = Handler()
        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH)
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(runnable)
    }
}
