package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.User
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] used for ProfileFragment
 */
@HiltViewModel
class ProfileViewModel  @Inject constructor(@ApplicationContext val context: Context, val userDataManager: UserDataManager): ViewModel() {

    var tag = ProfileViewModel::class.java.simpleName

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulUpdate: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var user: User
    lateinit var message: String

    /**
     * Fetches the current users full profile
     */
    @SuppressLint("CheckResult")
    fun getProfile() {
        userDataManager.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<User>() {
                    override fun onNext(userprofile: User) {
                        user = userprofile
                        successfulGet.value = true
                    }
                    override fun onError(throwable: Throwable) {
                        message = CommonUtils.getErrorMessage(context , throwable , tag)
                        successfulGet.value = false
                    }
                    override fun onComplete() {
                    }
                })
    }

    /**
     * Updates the current user profile with data changed by the user
     */
    @SuppressLint("CheckResult")
    fun updateProfile(user: User) {
        userDataManager.updateUser(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(response: CustomResponse) {
                        successfulUpdate.value = true
                    }
                    override fun onError(throwable: Throwable) {
                        message = CommonUtils.getErrorMessage(context , throwable , tag)
                        successfulUpdate.value = false
                    }
                    override fun onComplete() {
                    }
                })
    }
}

