package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.remote.datamanager.RelationDataManager
import org.systers.mentorship.remote.requests.RelationshipRequest
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] component used for the Send Request Activity
 */
@HiltViewModel
class SendRequestViewModel @Inject constructor(@ApplicationContext val context: Context, val relationDataManager: RelationDataManager): ViewModel() {

    var tag = SendRequestViewModel::class.java.simpleName

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * Call send a mentorship request service
     * @param relationshipRequest object containing mentorship request details
     */
    @SuppressLint("CheckResult")
    fun sendRequest(@NonNull relationshipRequest: RelationshipRequest) {
        relationDataManager.sendRequest(relationshipRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(customResponse: CustomResponse) {
                        message = customResponse.message ?: context
                                .getString(R.string.registration_successful)
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

