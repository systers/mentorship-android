package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.remote.datamanager.RelationDataManager
import org.anitab.mentorship.utils.CommonUtils

/**
 * This class represents the [ViewModel] used for Request Detail Screen
 */
class RequestDetailViewModel : ViewModel() {

    var tag = RequestDetailViewModel::class.java.simpleName

    private val relationDataManager = RelationDataManager()

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * Accepts a mentorship request
     * @param requestId id of the mentorship request
     */
    fun acceptRequest(requestId: Int) {
        viewModelScope.launch {
            try {
                message = relationDataManager.acceptRelationship(requestId).message
                successful.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successful.postValue(false)
            }
        }
    }

    /**
     * Rejects a mentorship request
     * @param requestId id of the mentorship request
     */
    fun rejectRequest(requestId: Int) {
        viewModelScope.launch {
            try {
                message = relationDataManager.rejectRelationship(requestId).message
                successful.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successful.postValue(false)
            }
        }
    }

    /**
     * Deletes a mentorship request
     * @param requestId id of the mentorship request
     */
    fun deleteRequest(requestId: Int) {
        viewModelScope.launch {
            try {
                message = relationDataManager.deleteRelationship(requestId).message
                successful.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successful.postValue(false)
            }
        }
    }
}
