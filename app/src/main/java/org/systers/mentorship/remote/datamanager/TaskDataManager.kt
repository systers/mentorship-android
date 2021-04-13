package org.systers.mentorship.remote.datamanager

import io.reactivex.Observable
import org.systers.mentorship.models.Task
import org.systers.mentorship.remote.ApiManager
import org.systers.mentorship.remote.requests.CreateTask
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.remote.services.TaskService
import javax.inject.Inject

/**
 * This class represents the data manager related to Mentorship Task API
 */
class TaskDataManager @Inject constructor(val taskService: TaskService){

    /**
     * This will call a method from Taskservice interface to fetch all tasks
     * @param relationId mentorship relation id
     * @return an Observable of [CustomResponse]
     */
    fun getAllTasks(relationId: Int): Observable<List<Task>> {
        return taskService.getAllTasksFromMentorshipRelation(relationId)
    }

    /**
     * This will call a method from Taskservice interface to complete a task
     * @param relationId mentorship relation id
     * @return an Observable of [CustomResponse]
     */
    fun completeTask(relationId: Int, taskId: Int): Observable<CustomResponse> {
        return taskService.completeTaskFromMentorshipRelation(relationId,taskId)
    }

    /**
     * This will call a method from Taskservice interface to add task for a relationship
     * @param relationId mentorship relation id
     * @param createTask object to serialize task description
     * @return an Observable of [CustomResponse]
     */
    fun addTask(relationId: Int, createTask: CreateTask): Observable<CustomResponse> {
        return taskService.addTaskToMentorshipRelation(relationId,createTask)
    }

}
