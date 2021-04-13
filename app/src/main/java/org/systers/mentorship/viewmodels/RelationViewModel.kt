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
import org.systers.mentorship.models.Relationship
import org.systers.mentorship.remote.datamanager.RelationDataManager
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] component used for the Sign Up Activity
 */
@HiltViewModel
class RelationViewModel  @Inject constructor(@ApplicationContext val context: Context, val relationDataManager: RelationDataManager) : ViewModel() {

    var tag = RelationViewModel::class.java.simpleName

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulCancel: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var mentorshipRelation: Relationship
    lateinit var message: String

    /**
     * Fetches current relation details
     */
    @SuppressLint("CheckResult")
    fun getCurrentRelationDetails() {
        relationDataManager.getCurrentRelationship()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Relationship>() {
                    override fun onNext(relationship: Relationship) {
                        mentorshipRelation = relationship
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
     * Cancels a mentorship relation
     */
    @SuppressLint("CheckResult")
    fun cancelMentorshipRelation(relationId: Int) {
        relationDataManager.cancelRelationship(relationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(customResponse: CustomResponse) {
                        message = customResponse.message
                        successfulCancel.value = true
                    }

                    override fun onError(throwable: Throwable) {
                        message = CommonUtils.getErrorMessage(context , throwable , tag)
                        successfulCancel.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }
}

