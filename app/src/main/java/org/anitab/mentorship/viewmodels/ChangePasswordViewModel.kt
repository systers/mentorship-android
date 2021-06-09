package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.remote.datamanager.UserDataManager
import org.anitab.mentorship.remote.requests.ChangePassword
import org.anitab.mentorship.utils.CommonUtils

/**
 * This class represents the [ViewModel] used for ChangePasswordFragment
 */
class ChangePasswordViewModel : ViewModel() {

    private val tag = this::class.java.simpleName
    private val userDataManager: UserDataManager = UserDataManager()
    val successfulUpdate: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * Updates the password of the current user
     */
    fun changeUserPassword(changePassword: ChangePassword) {
        viewModelScope.launch {
            try {
                userDataManager.updatePassword(changePassword)
                successfulUpdate.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successfulUpdate.postValue(false)
            }
        }
    }
}
