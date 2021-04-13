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
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] used for Requests Screen
 */
@HiltViewModel
class RequestsViewModel @Inject constructor(@ApplicationContext val context: Context, val relationDataManager: RelationDataManager) : ViewModel() {

    var tag = RequestsViewModel::class.java.simpleName

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    val pendingSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var pendingAllRequestsList: List<Relationship>

    var message: String? = null
    var allRequestsList: List<Relationship>? = null
    var pastRequestsList: List<Relationship>? = null
    /**
     * Fetches list of all Mentorship relations and requests
     */
    @SuppressLint("CheckResult")
    fun getAllMentorshipRelations() {
        relationDataManager.getAllRelationsAndRequests()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Relationship>>() {
                    override fun onNext(relationsList: List<Relationship>) {
                        allRequestsList = relationsList.sortedByDescending{ it.creationDate }
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

    /**
     * Fetches list of all pending Mentorship relations and requests
     */
    @SuppressLint("CheckResult")
    fun getAllPendingMentorshipRelations() {
        relationDataManager.getAllPendingRelationsAndRequests()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<Relationship>>() {
                override fun onNext(relationsList: List<Relationship>) {
                    pendingAllRequestsList = relationsList.sortedByDescending{ it.creationDate }
                    pendingSuccessful.value = true
                }

                override fun onError(throwable: Throwable) {
                    message = CommonUtils.getErrorMessage(context , throwable , tag)
                    pendingSuccessful.value = false
                }

                override fun onComplete() {
                }
            })
    }


    /* past mentorship relations working:
       1. RelationshipService.kt makes API call to mentorship_relations/past
       2. getPastRelationships() in RelationDataManager.kt reads this as Observable<List<Relationship>>
       3. getPastMentorshipRelations() in RequestsViewModel.kt subscribes to the data and manages exception handling
       4. getPastMentorshipRelations() called in RequestsFragment.kt. It displays this data in fragment_requests.xml
        */
    @SuppressLint("CheckResult")
    fun getPastMentorshipRelations() {
        relationDataManager.getPastRelationships()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<Relationship>>() {
                override fun onNext(relationsList: List<Relationship>) {
                    pastRequestsList = relationsList.sortedByDescending{ it.creationDate }
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



