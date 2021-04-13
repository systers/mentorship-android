package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.Task
import org.systers.mentorship.remote.datamanager.RelationDataManager
import org.systers.mentorship.remote.datamanager.TaskDataManager
import org.systers.mentorship.remote.requests.CreateTask
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * This class represents the [ViewModel] used for Tasks Screen
 */
class TasksViewModel@Inject constructor(@ApplicationContext val context: Context, val taskDataManager: TaskDataManager): ViewModel() {

    var tag = TasksViewModel::class.java.simpleName

    lateinit var tasksList: List<Task>
    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAdd: MutableLiveData<Boolean> = MutableLiveData()
    val successfulUpdate: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    var completeTasksList ={
        -> tasksList.filter { it.isDone }
    }
    var incompleteTasksList ={
        -> tasksList.filter { !it.isDone }
    }

    /**
     * This function lists all tasks from the mentorship relation
     */
    @SuppressLint("CheckResult")
    fun getTasks(relationId: Int) {
        taskDataManager.getAllTasks(relationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Task>>() {
                    override fun onNext(taskListResponse: List<Task>) {
                        tasksList = taskListResponse
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
     * This function helps in adds a new task to the task list
     * @param relationId relation for which task is to be added
     * @param createTask to serialize task description
     */
    @SuppressLint("CheckResult")
    fun addTask(relationId: Int, createTask: CreateTask) {
        taskDataManager.addTask(relationId, createTask)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(taskListResponse: CustomResponse) {
                        successfulAdd.value = true
                    }
                    override fun onError(throwable: Throwable) {
                        message = CommonUtils.getErrorMessage(context , throwable , tag)
                        successfulAdd.value = false
                    }
                    override fun onComplete() {
                    }
                })
    }

    /**
     * This function helps in updating completed tasks
     * @param taskId id of the task that is clicked
     * @param isChecked boolean value to specify if the task was marked or unmarked
     * @param relationId id of relation
     */
    @SuppressLint("CheckResult")
    fun updateTask(taskId: Int, isChecked: Boolean, relationId: Int){
        if(isChecked) {
            taskDataManager.completeTask(relationId,taskId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<CustomResponse>() {
                        override fun onNext(taskListResponse: CustomResponse) {
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
        else {
            //completedTaskList.remove(taskList.get(taskId))
            //TODO: Update the backend
        }
    }
}
