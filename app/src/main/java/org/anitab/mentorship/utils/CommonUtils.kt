package org.anitab.mentorship.utils

import android.util.Log
import androidx.annotation.NonNull
import com.google.gson.Gson
import java.io.IOException
import java.util.concurrent.TimeoutException
import org.anitab.mentorship.MentorshipApplication
import org.anitab.mentorship.R
import org.anitab.mentorship.remote.responses.CustomResponse
import retrofit2.HttpException

/**
 * Object to store utilities such as a [Gson] instance
 */
object CommonUtils {

    val gson = Gson()

    /**
     * Extracts a CustomResponse object from a throwable
     * @param throwable from which the object is to be extracted
     * @return a CustomResponse object
     */
    fun getErrorResponse(@NonNull throwable: Throwable): CustomResponse {
        val httpException = throwable as HttpException
        val response = httpException.response()?.errorBody()?.string()
        return gson.fromJson(response.toString(), CustomResponse::class.java)
    }

    fun getErrorMessage(@NonNull throwable: Throwable, tag: String): String {
        return when (throwable) {
            is IOException -> {
                MentorshipApplication.getContext()
                        .getString(R.string.error_please_check_internet)
            }
            is TimeoutException -> {
                MentorshipApplication.getContext()
                        .getString(R.string.error_request_timed_out)
            }
            is HttpException -> {
                getErrorResponse(throwable).message
            }
            else -> {
                Log.e(tag, throwable.localizedMessage)
                MentorshipApplication.getContext()
                        .getString(R.string.error_something_went_wrong)
            }
        }
    }
}
