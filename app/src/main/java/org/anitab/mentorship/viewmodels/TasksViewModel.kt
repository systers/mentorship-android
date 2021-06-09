package org.anitab.mentorship.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.anitab.mentorship.models.Task
import org.anitab.mentorship.remote.datamanager.TaskDataManager
import org.anitab.mentorship.remote.requests.CreateTask
import org.anitab.mentorship.utils.CommonUtils

/**
 * This class represents the [ViewModel] used for Tasks Screen
 */
class TasksViewModel : ViewModel() {

    var tag = TasksViewModel::class.java.simpleName

    lateinit var tasksList: List<Task>

    private val taskDataManager: TaskDataManager = TaskDataManager()
    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulAdd: MutableLiveData<Boolean> = MutableLiveData()
    val successfulUpdate: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    var completeTasksList = {
        ->
        tasksList.filter { it.isDone }
    }
    var incompleteTasksList = {
        ->
        tasksList.filter { !it.isDone }
    }

    /**
     * This function lists all tasks from the mentorship relation
     */
    fun getTasks(relationId: Int) {
        viewModelScope.launch {
            try {
                tasksList = taskDataManager.getAllTasks(relationId)
                successfulGet.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successfulGet.postValue(false)
            }
        }
    }

    /**
     * This function helps in adds a new task to the task list
     * @param relationId relation for which task is to be added
     * @param createTask to serialize task description
     */
    fun addTask(relationId: Int, createTask: CreateTask) {
        viewModelScope.launch {
            try {
                taskDataManager.addTask(relationId, createTask)
                successfulAdd.postValue(true)
            } catch (throwable: Throwable) {
                message = CommonUtils.getErrorMessage(throwable, tag)
                successfulAdd.postValue(false)
            }
        }
    }

    /**
     * This function helps in updating completed tasks
     * @param taskId id of the task that is clicked
     * @param isChecked boolean value to specify if the task was marked or unmarked
     * @param relationId id of relation
     */
    fun updateTask(taskId: Int, isChecked: Boolean, relationId: Int) {
        viewModelScope.launch {
            if (isChecked) {
                try {
                    taskDataManager.completeTask(relationId, taskId)
                    successfulUpdate.postValue(true)
                } catch (throwable: Throwable) {
                    message = CommonUtils.getErrorMessage(throwable, tag)
                    successfulUpdate.postValue(false)
                }
            } else {
                // TODO UpdateBackend
            }
        }
    }
}
