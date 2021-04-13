package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.User
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] component used for the MemberProfileActivity
 */
@HiltViewModel
class MemberProfileViewModel  @Inject constructor(@ApplicationContext val context : Context , val userDataManager: UserDataManager): ViewModel() {

    var tag = MemberProfileViewModel::class.java.simpleName

    val successful: MediatorLiveData<Boolean> = MediatorLiveData()
    lateinit var message: String
    lateinit var userProfile: User
    var userId = -1

    /**
     * Fetches profile from a user with the value of [userId] by calling getUser method from UserService
     */
    @SuppressLint("CheckResult")
    fun getUserProfile() {
        userDataManager.getUser(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<User>() {
                    override fun onNext(user: User) {
                        userProfile = user
                        successful.value = true
                    }

                    override fun onError(throwable: Throwable) {
                        when (throwable) {
                            is IOException -> {
                                message = context
                                        .getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                message = context
                                        .getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                message = CommonUtils.getErrorResponse(throwable).message
                            }
                            else -> {
                                message = context.getString(R.string.error_something_went_wrong)
                                Log.e(tag, throwable.localizedMessage)
                            }
                        }
                        successful.value = false
                    }

                    override fun onComplete() {
                    }
                })

    }
}

