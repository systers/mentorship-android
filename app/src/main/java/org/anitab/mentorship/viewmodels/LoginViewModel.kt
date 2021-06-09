package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.remote.datamanager.AuthDataManager
import org.anitab.mentorship.remote.requests.Login
import org.anitab.mentorship.utils.CommonUtils
import org.anitab.mentorship.utils.PreferenceManager

/**
 * This class represents the [ViewModel] component used for the Login Activity
 */
class LoginViewModel : ViewModel() {

    var tag = LoginViewModel::class.java.simpleName

    private val preferenceManager: PreferenceManager = PreferenceManager()
    private val authDataManager: AuthDataManager = AuthDataManager()

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * Will be used to run the login method of the AuthService
     * @param login a login request object containing the credentials
     */
    fun login(login: Login) {
        viewModelScope.launch {
            try {
                preferenceManager.putAuthToken(authDataManager.login(login).accessToken)
                successful.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successful.postValue(false)
            }
        }
    }
}
