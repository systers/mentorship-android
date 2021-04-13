package org.systers.mentorship.di

import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.systers.mentorship.remote.responses.JwtPayload
import org.systers.mentorship.utils.CommonUtils
import org.systers.mentorship.utils.PreferenceManager
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class JWTModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Authtoken

    /**
     * Decodes a JSON Web Token's header and body
     * @param jwt JSON Web Token in string format
     * @return body/payload of jwt Base64 decoded
     */
    private fun decodeJwtPayload(jwt: String): String {
        Log.d("JWT itself : ", jwt)
        val jwtParsed = jwt.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val base64EncodedBody = jwtParsed[1]
        val base64DecodedBody = Base64.decode(base64EncodedBody, Base64.DEFAULT)

        val body = String(base64DecodedBody)
        Log.d("JWT Body : ", body)

        return body
    }

    /**
    * Provides the auth token from the shared preferences.
     * This method is for hilt and should not be called.
    * */
    @Authtoken
    @Provides
    fun provideAuthToken(preferenceManager: PreferenceManager): String {
        return preferenceManager.authToken
    }

    /**
     * Provides the auth token payload.
     * This method is for hilt and should not be called.
     * */
    @Provides
    fun provideAuthTokenPayload(@Authtoken authToken: String ,gson: Gson): JwtPayload {
        val decodedJwtBody = decodeJwtPayload(authToken)
        return gson.fromJson(decodedJwtBody, JwtPayload::class.java)
    }

}