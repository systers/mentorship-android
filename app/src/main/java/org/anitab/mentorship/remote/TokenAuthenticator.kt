package org.anitab.mentorship.remote

import android.content.Intent
import androidx.core.content.ContextCompat
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.anitab.mentorship.MentorshipApplication
import org.anitab.mentorship.utils.Constants
import org.anitab.mentorship.utils.PreferenceManager
import org.anitab.mentorship.view.activities.LoginActivity

class TokenAuthenticator : Authenticator {

    private val preferenceManager: PreferenceManager = PreferenceManager()
    private val LOGIN_PATH = "/login"
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code() == 401) {

            if (LOGIN_PATH == response.request().url().encodedPath()) {
                return null
            }

            preferenceManager.clear()
            val intent = Intent(MentorshipApplication.getContext(), LoginActivity::class.java)
            intent.putExtra(Constants.TOKEN_EXPIRED_EXTRA, 0)
            ContextCompat.startActivity(MentorshipApplication.getContext(), intent, null)
            return null
        }
        return response.request().newBuilder().build()
    }
}
