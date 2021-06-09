package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.remote.datamanager.RelationDataManager
import org.anitab.mentorship.remote.requests.RelationshipRequest
import org.anitab.mentorship.utils.CommonUtils

/**
 * This class represents the [ViewModel] component used for the Send Request Activity
 */
class SendRequestViewModel : ViewModel() {

    var tag = SendRequestViewModel::class.java.simpleName

    private val relationDataManager: RelationDataManager = RelationDataManager()

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * Call send a mentorship request service
     * @param relationshipRequest object containing mentorship request details
     */
    fun sendRequest(relationshipRequest: RelationshipRequest) {
        viewModelScope.launch {
            try {
                message = relationDataManager.sendRequest(relationshipRequest).message
                successful.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successful.postValue(false)
            }
        }
    }
}
