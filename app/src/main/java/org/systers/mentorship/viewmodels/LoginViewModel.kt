package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.remote.datamanager.AuthDataManager
import org.systers.mentorship.remote.requests.Login
import org.systers.mentorship.remote.responses.AuthToken
import org.systers.mentorship.utils.CommonUtils
import org.systers.mentorship.utils.PreferenceManager
import javax.inject.Inject

/**
 * This class represents the [ViewModel] component used for the Login Activity
 */
@HiltViewModel
class LoginViewModel @Inject constructor(@ApplicationContext val context : Context, val authDataManager: AuthDataManager, val preferenceManager: PreferenceManager): ViewModel() {

    var tag = LoginViewModel::class.java.simpleName
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * Will be used to run the login method of the AuthService
     * @param login a login request object containing the credentials
     */
    @SuppressLint("CheckResult")
    fun login(@NonNull login: Login) {
        authDataManager.login(login)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<AuthToken>() {
                    override fun onNext(authToken: AuthToken) {
                        successful.value = true
                        preferenceManager.putAuthToken(authToken.accessToken)
                    }

                    override fun onError(throwable: Throwable) {
                       message = CommonUtils.getErrorMessage(context,throwable,tag)
                        successful.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }
}

