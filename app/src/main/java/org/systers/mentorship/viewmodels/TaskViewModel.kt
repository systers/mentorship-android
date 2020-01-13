package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.Task
import org.systers.mentorship.remote.datamanager.TaskDataManager
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 * This class represents the [ViewModel] used for Tasks Screen
 */
class TasksViewModel: ViewModel() {

    var TAG = TasksViewModel::class.java.simpleName

    lateinit var tasksList: List<Task>

    private val taskDataManager: TaskDataManager = TaskDataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    /**
     * This function lists all tasks from the mentorship relation
     */
    fun getTasks(relationId: Int) {
        taskDataManager.getAllTasks(relationId).process { list, throwable ->
            if (throwable != null) {
                throwable.printStackTrace()
                message = throwable.localizedMessage
                successful.postValue(false)
            } else {
                if (list != null) {
                    successful.postValue(true)
                    tasksList = list
                } else {
                    successful.postValue(false)
                }
            }
        }
    }

    /**
     * This function helps in adds a new task to the task list
     * @param taskName title of the new task
     */
    fun addTask(taskName: String) {
        //TODO: Update the backend
    }

    /**
     * This function helps in updating completed tasks
     * @param taskId id of the task that is clicked
     * @param isChecked boolean value to specify if the task was marked or unmarked
     */
    fun updateTask(taskId: Int, isChecked: Boolean){
        if(isChecked) {
            //completedTaskList.add(taskList.get(taskId))
            //TODO: Update the backend
        }
        else {
            //completedTaskList.remove(taskList.get(taskId))
            //TODO: Update the backend
        }
    }
}
