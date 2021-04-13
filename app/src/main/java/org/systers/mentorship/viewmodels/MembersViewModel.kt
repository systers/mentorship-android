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
import org.systers.mentorship.remote.requests.PaginationRequest
import org.systers.mentorship.utils.CommonUtils
import org.systers.mentorship.utils.Constants.ITEMS_PER_PAGE
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] component used for the Members Activity
 */
@HiltViewModel
class MembersViewModel  @Inject constructor(@ApplicationContext val context: Context , val userDataManager: UserDataManager) : ViewModel() {

    var tag = MembersViewModel::class.java.simpleName

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    var currentPage = 1
    lateinit var message: String
    var userList: ArrayList<User> = arrayListOf()


    /**
     * Fetches users list from getUsers method of the UserService
     */
    @SuppressLint("CheckResult")
    fun getUsers(isRefresh: Boolean) {
        if (isRefresh) {
            userList.clear()
            currentPage = 1
        }
        userDataManager.getUsers(paginationRequest = PaginationRequest(currentPage,ITEMS_PER_PAGE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<User>>() {
                    override fun onNext(userListResponse: List<User>) {
                        userList.addAll(userListResponse)
                        successful.value = true
                        currentPage++
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

