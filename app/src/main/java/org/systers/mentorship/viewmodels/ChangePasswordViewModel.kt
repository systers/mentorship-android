package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.remote.requests.ChangePassword
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import javax.inject.Inject

/**
 * This class represents the [ViewModel] used for ChangePasswordFragment
 */
@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ChangePasswordViewModel  @Inject constructor(
         @ApplicationContext val context : Context,
        val userDataManager: UserDataManager )
    : ViewModel() {
    val successfulUpdate: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String
    /**
     * Updates the password of the current user
     */
    @SuppressLint("CheckResult")
    fun changeUserPassword(changePassword: ChangePassword) {
        userDataManager.updatePassword(changePassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(customResponse: CustomResponse) {
                        message = customResponse.message
                        successfulUpdate.value = true
                    }

                    override fun onError(e: Throwable) {
                        message = CommonUtils.getErrorMessage(context,e,this.javaClass.simpleName)
                        successfulUpdate.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }
}
