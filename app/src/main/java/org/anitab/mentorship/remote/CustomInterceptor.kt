package org.anitab.mentorship.remote

import android.text.TextUtils
import androidx.annotation.NonNull
import okhttp3.Interceptor
import okhttp3.Response
import org.anitab.mentorship.utils.PreferenceManager

/**
 * Represents a custom HTTP requests interceptor
 */
class CustomInterceptor : Interceptor {

    var preferenceManager: PreferenceManager = PreferenceManager()

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
