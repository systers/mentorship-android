package org.systers.mentorship.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.Relationship
import org.systers.mentorship.remote.datamanager.RelationDataManager
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 * This class represents the [ViewModel] component used for the Sign Up Activity
 */
class RelationViewModel : ViewModel() {
    private val TAG = RelationViewModel::class.java.simpleName

    private val relationDataManager: RelationDataManager = RelationDataManager()

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulCancel: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var mentorshipRelation: Relationship
    lateinit var message: String

    /**
     * Fetches current relation details
     */
    fun getCurrentRelationDetails() = viewModelScope.launch {
        try {
            mentorshipRelation = relationDataManager.getCurrentRelationship()
            successfulGet.value = true
        } catch (throwable: Throwable) {
            message = when (throwable) {
                is IOException -> {
                    MentorshipApplication.getContext().getString(
                            R.string.error_please_check_internet)
                }
                is TimeoutException -> {
                    MentorshipApplication.getContext().getString(R.string.error_request_timed_out)
                }
                is HttpException -> {
                    CommonUtils.getErrorResponse(throwable).message
                }
                else -> {
                    Log.e(TAG, throwable.localizedMessage)
                    MentorshipApplication.getContext().getString(
                            R.string.error_something_went_wrong)
                }
            }
            successfulGet.value = false
        }
    }

    /**
     * Cancels a mentorship relation
     */
    fun cancelMentorshipRelation(relationId: Int) = viewModelScope.launch {
        try {
            message = relationDataManager.cancelRelationship(relationId).message
            successfulCancel.value = true
        } catch (throwable: Throwable) {
            message = when (throwable) {
                is IOException -> {
                    MentorshipApplication.getContext().getString(
                            R.string.error_please_check_internet)
                }
                is TimeoutException -> {
                    MentorshipApplication.getContext().getString(R.string.error_request_timed_out)
                }
                is HttpException -> {
                    CommonUtils.getErrorResponse(throwable).message
                }
                else -> {
                    Log.e(TAG, throwable.localizedMessage)
                    MentorshipApplication.getContext().getString(
                            R.string.error_something_went_wrong)
                }
            }
            successfulCancel.value = false
        }
    }
}
