package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.remote.datamanager.RelationDataManager
import org.anitab.mentorship.utils.CommonUtils

/**
 * This class represents the [ViewModel] component used for the Sign Up Activity
 */
class RelationViewModel : ViewModel() {

    var tag = RelationViewModel::class.java.simpleName

    private val relationDataManager: RelationDataManager = RelationDataManager()

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulCancel: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var mentorshipRelation: Relationship
    lateinit var message: String

    /**
     * Fetches current relation details
     */
    fun getCurrentRelationDetails() {
        viewModelScope.launch {
            try {
                mentorshipRelation = relationDataManager.getCurrentRelationship()
                successfulGet.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successfulGet.postValue(false)
            }
        }
    }

    /**
     * Cancels a mentorship relation
     */
    fun cancelMentorshipRelation(relationId: Int) {
        viewModelScope.launch {
            try {

                message = relationDataManager.cancelRelationship(relationId).message
                successfulCancel.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successfulCancel.postValue(false)
            }
        }
    }
}
