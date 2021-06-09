package org.anitab.mentorship.view.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_list_item.view.*
import org.anitab.mentorship.R
import org.anitab.mentorship.models.Task

/**
 * This class represents the adapter that fills in each view of the Tasks recyclerView
 * @param tasksList list of tasks taken up by the mentee
 * @param markTask function to be called when an item from Tasks list is clicked
 * @param complete whether task is complete or not
 */
class TasksAdapter(
    private val context: Context,
    private val tasksList: List<Task>,
    private val markTask: (taskId: Int) -> Unit,
    private val complete: Boolean
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
            TaskViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.task_list_item, parent, false))

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = tasksList[position]
        val itemView = holder.itemView
        /** Working: Functionality to add new tasks and achievements
        1. TaskService.kt makes PUT, POST and GET requests for tasks and get <CustomResponse>.
        requests/CreateTask.kt used for serializing "description" field to create task.
        2. TaskDataManager.kt: completeTask() and addTask() call TaskService.kt
        3. TaskViewModel.kt: updateTask() and addTask() subscribe to the responses
        4. TasksFragment.kt:
        - After calling getTask() incomplete tasks are added to to do list, completed tasks to Achievements
        - fabAddItem: Floatbutton opens dialog box to create task. Call is made to addTask()
        - Listeners added for task checkboxes. completeTask() is called when clicked.
        5. Layout changes
        - ScrollView added to fragment_mentorship_tasks.xml for scrolling
        - to do and achievements can be toggled by clicking arrow buttons (listener attached)
         **/
        itemView.cbTask.text = item.description
        if (complete) {
            itemView.cbTask.isChecked = true
            itemView.cbTask.isClickable = false
        } else {
            itemView.cbTask.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(context.getString(R.string.mark_task_title))
                builder.setMessage(context.getString(R.string.mark_task_message))
                builder.setPositiveButton(context.getString(R.string.yes)) { dialog, which ->
                    itemView.cbTask.isChecked = true
                    markTask(item.id)
                }
                builder.setNegativeButton(context.getString(R.string.no)) { dialog, which ->
                    itemView.cbTask.isChecked = false
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    override fun getItemCount(): Int = tasksList.size

    /**
     * This class holds a view for each item of the Tasks list
     * @param itemView represents each view of Tasks list
     */
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
