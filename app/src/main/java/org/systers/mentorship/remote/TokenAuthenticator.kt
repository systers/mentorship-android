package org.systers.mentorship.remote

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.utils.PreferenceManager
import org.systers.mentorship.view.activities.LoginActivity
import java.lang.annotation.Inherited
import javax.inject.Inject
class TokenAuthenticator @Inject constructor(@ApplicationContext val context : Context) : Authenticator{
    @Inject
    lateinit var preferenceManager: PreferenceManager
    private val LOGIN_PATH = "/login"
    override fun authenticate(route: Route, response: Response): Request? {
        if (response.code() == 401) {

            if (LOGIN_PATH == response.request().url().encodedPath()) {
                return null
            }

            preferenceManager.clear()
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(Constants.TOKEN_EXPIRED_EXTRA, 0)
            ContextCompat.startActivity(context, intent, null)
            return null
        }
        return response.request().newBuilder().build()
    }
}
