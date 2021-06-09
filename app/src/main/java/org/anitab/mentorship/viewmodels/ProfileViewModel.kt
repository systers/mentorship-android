package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.models.User
import org.anitab.mentorship.remote.datamanager.UserDataManager
import org.anitab.mentorship.utils.CommonUtils

/**
 * This class represents the [ViewModel] used for ProfileFragment
 */
class ProfileViewModel : ViewModel() {

    var tag = ProfileViewModel::class.java.simpleName

    private val userDataManager: UserDataManager = UserDataManager()

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulUpdate: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var user: User
    lateinit var message: String

    /**
     * Fetches the current users full profile
     */
    fun getProfile() {
        viewModelScope.launch {
            try {
                user = userDataManager.getUser()
                successfulGet.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successfulGet.postValue(false)
            }
        }
    }

    /**
     * Updates the current user profile with data changed by the user
     */
    fun updateProfile(user: User) {
        viewModelScope.launch {
            try {
                userDataManager.updateUser(user)
                successfulUpdate.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successfulUpdate.postValue(false)
            }
        }
    }
}
