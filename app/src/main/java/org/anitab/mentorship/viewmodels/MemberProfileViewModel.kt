package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.models.User
import org.anitab.mentorship.remote.datamanager.UserDataManager
import org.anitab.mentorship.utils.CommonUtils

/**
 * This class represents the [ViewModel] component used for the MemberProfileActivity
 */
class MemberProfileViewModel : ViewModel() {

    var tag = MemberProfileViewModel::class.java.simpleName

    private val userDataManager: UserDataManager = UserDataManager()

    val successful: MediatorLiveData<Boolean> = MediatorLiveData()
    lateinit var message: String
    lateinit var userProfile: User
    var userId = -1

    /**
     * Fetches profile from a user with the value of [userId] by calling getUser method from UserService
     */
    fun getUserProfile() {
        viewModelScope.launch {
            try {
                userProfile = userDataManager.getUser(userId)
                successful.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successful.postValue(false)
            }
        }
    }
}
