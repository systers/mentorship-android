package org.anitab.mentorship.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.models.HomeStatistics
import org.anitab.mentorship.remote.datamanager.UserDataManager
import org.anitab.mentorship.utils.CommonUtils
import org.anitab.mentorship.utils.SingleLiveEvent

/**
 * This class represents the ViewModel for the HomeFragment
 */
class HomeViewModel : ViewModel() {

    private val tag = this::class.java.simpleName
    private val userDataManager by lazy { UserDataManager() }

    private val _userStats = MutableLiveData<HomeStatistics>()
    private val _message = SingleLiveEvent<String>()

    val userStats: LiveData<HomeStatistics>
        get() = _userStats

    val message: LiveData<String>
        get() = _message

    /**
     * Fetches home stats from getHomeStats method of the UserService
     */

    fun getHomeStats() {
        viewModelScope.launch {
            try {
                _userStats.postValue(userDataManager.getHomeStats())
            } catch (throwable: Throwable) {
                _message.postValue(CommonUtils.getErrorMessage(throwable, tag))
            }
        }
    }
}
