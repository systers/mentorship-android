package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.remote.datamanager.AuthDataManager
import org.systers.mentorship.remote.requests.Login
import org.systers.mentorship.remote.responses.AuthToken
import org.systers.mentorship.utils.CommonUtils
import org.systers.mentorship.utils.PreferenceManager
import retrofit2.HttpException

/**
 * This class represents the [ViewModel] component used for the Login Activity
 */
class LoginViewModel : ViewModel() {

    var tag = LoginViewModel::class.java.simpleName!!

    private val preferenceManager: PreferenceManager = PreferenceManager()
    private val authDataManager: AuthDataManager = AuthDataManager()

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    private val _successful = MutableLiveData(false)
    val successful: LiveData<Boolean> = _successful

    val message = MutableLiveData<String>()

    /**
     * Will be used to run the login method of the AuthService
     * @param login a login request object containing the credentials
     */
    @SuppressLint("CheckResult")
    fun login(@NonNull login: Login) {
        _buttonEnabled.value = false
        authDataManager.login(login)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<AuthToken>() {
                    override fun onNext(authToken: AuthToken) {
                        _successful.value = true
                        preferenceManager.putAuthToken(authToken.accessToken)
                    }

                    override fun onError(throwable: Throwable) {
                        when (throwable) {
                            is IOException -> {
                                message.value = MentorshipApplication.getContext()
                                        .getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                message.value = MentorshipApplication.getContext()
                                        .getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                message.value = CommonUtils.getErrorResponse(throwable).message
                            }
                            else -> {
                                message.value = MentorshipApplication.getContext()
                                        .getString(R.string.error_something_went_wrong)
                                Log.e(tag, throwable.localizedMessage)
                            }
                        }
                        _successful.value = false
                        _buttonEnabled.value = true
                    }

                    override fun onComplete() {
                        _buttonEnabled.value = false
                    }
                })
    }

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
        _buttonEnabled.value = !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _buttonEnabled.value = !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
    }

    fun onButtonClick() {
        message.value = ""
        login(Login(username.value!!, password.value!!))
    }
}
