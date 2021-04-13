package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.remote.datamanager.AuthDataManager
import org.systers.mentorship.remote.datamanager.RelationDataManager
import org.systers.mentorship.remote.requests.Register
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] component used for the Sign Up Activity
 */
class SignUpViewModel @Inject constructor(@ApplicationContext val context: Context, val authDataManager: AuthDataManager): ViewModel() {

    var tag = SignUpViewModel::class.java.simpleName

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * Will be used to run the register method of the AuthService
     * @param register a registration request object containing the a user's registration fields
     */
    @SuppressLint("CheckResult")
    fun register(@NonNull register: Register) {
        authDataManager.register(register)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(customResponse: CustomResponse) {
                        message = customResponse.message
                        successful.value = true
                    }

                    override fun onError(throwable: Throwable) {
                        message = CommonUtils.getErrorMessage(context , throwable , tag)
                        successful.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }
}

