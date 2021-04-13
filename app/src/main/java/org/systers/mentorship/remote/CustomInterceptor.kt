package org.systers.mentorship.remote

import androidx.annotation.NonNull
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response
import org.systers.mentorship.utils.PreferenceManager
import javax.inject.Inject

/**
 * Represents a custom HTTP requests interceptor
 */
class CustomInterceptor @Inject constructor(): Interceptor {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun intercept(@NonNull chain: Interceptor.Chain): Response {

        val chainRequest = chain.request()
        val builder = chainRequest.newBuilder()

        val accessToken = preferenceManager.authToken

        if (!TextUtils.isEmpty(accessToken)) {
            builder.header("Authorization", preferenceManager.authToken)
        }

        val request = builder.build()
        return chain.proceed(request)
    }
}
