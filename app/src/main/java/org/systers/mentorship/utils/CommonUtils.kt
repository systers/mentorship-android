package org.systers.mentorship.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import io.reactivex.annotations.NonNull
import org.systers.mentorship.R
import org.systers.mentorship.remote.responses.CustomResponse
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException


/**
 * Object to store utilities such as a [Gson] instance
 */

//TODO use di here
object CommonUtils {

    val gson = Gson()

    /**
     * Extracts a CustomResponse object from a throwable
     * @param throwable from which the object is to be extracted
     * @return a CustomResponse object
     */
    fun getErrorResponse(@NonNull throwable: Throwable): CustomResponse {
        val httpException = throwable as HttpException
        val response = httpException.response().errorBody()?.string()
        return gson.fromJson(response.toString(), CustomResponse::class.java)
    }

    fun getErrorMessage(context: Context,error: Throwable, tag: String): String {
        return when (error) {
            is IOException -> {
                context.getString(R.string.error_please_check_internet)
            }
            is TimeoutException -> {
                (context.getString(R.string.error_request_timed_out))
            }
            is HttpException -> {
                (getErrorResponse(error).message)
            }
            else -> {
                (context.getString(R.string.error_something_went_wrong))
                        .also { Log.d(tag, error.localizedMessage) }
            }
        }
    }
}
