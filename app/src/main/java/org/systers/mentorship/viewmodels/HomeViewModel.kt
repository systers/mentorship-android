package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.models.HomeStatistics
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.utils.CommonUtils
import org.systers.mentorship.utils.SingleLiveEvent
import javax.inject.Inject


/**
 * This class represents the ViewModel for the HomeFragment
 */
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeViewModel @Inject constructor(@ApplicationContext val context: Context,
                                        val userDataManager: UserDataManager) : ViewModel() {

    private val tag = this::class.java.simpleName
    private val compositeDisposable by lazy { CompositeDisposable() }
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
        userDataManager.getHomeStats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<HomeStatistics>() {

                    override fun onComplete() {
                        // Do nothing
                    }

                    override fun onNext(statistics: HomeStatistics) {
                        _userStats.value = statistics
                    }

                    override fun onError(error: Throwable) {
                        _message.value = CommonUtils.getErrorMessage(context,error,tag)
                    }

                })
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}


