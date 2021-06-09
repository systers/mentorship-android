package org.systers.mentorship.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.dialog_add_task.view.*
import kotlinx.android.synthetic.main.fragment_mentorship_tasks.*
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.Relationship
import org.systers.mentorship.remote.requests.CreateTask
import org.systers.mentorship.view.adapters.TasksAdapter
import org.systers.mentorship.viewmodels.TasksViewModel

@SuppressLint("ValidFragment")
/**
 * The fragment is responsible for showing the all mentorship tasks
 * and achievements. It also allows to add new tasks and mark them as complete.
 */
class TasksFragment(private var mentorshipRelation: Relationship) : BaseFragment() {

    companion object {
        /**
         * Creates an instance of [TasksFragment]
         */
        fun newInstance(mentorshipRelation: Relationship) = TasksFragment(mentorshipRelation)
        val TAG = TasksFragment::class.java.simpleName
    }

    val appContext = MentorshipApplication.getContext()
    private val taskViewModel: TasksViewModel by viewModels()

    override fun getLayoutResourceId(): Int = R.layout.fragment_mentorship_tasks

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fabAddItem.setOnClickListener {
            showCompleteDialog()
        }
        imageView.setOnClickListener {
            if (rvTasks.visibility == View.GONE) {
                rvTasks.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            } else {
                rvTasks.visibility = View.GONE
                imageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
            }
        }
        imageView2.setOnClickListener {
            if (rvAchievements.visibility == View.GONE) {
                rvAchievements.visibility = View.VISIBLE
                imageView2.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            } else {
                rvAchievements.visibility = View.GONE
                imageView2.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
            }
        }
        // get tasks
        taskViewModel.successfulGet.observe(viewLifecycleOwner, {

            successful ->
            if (successful != null) {
                if (successful) {
                    if (taskViewModel.tasksList.isEmpty()) {
                        tvNoTask.visibility = View.VISIBLE
                        rvTasks.visibility = View.GONE
                    } else {
                        rvTasks.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = TasksAdapter(requireContext(), taskViewModel.incompleteTasksList(), ::markTask, false)
                        }
                        tvNoTask.visibility = View.GONE

                        val completeTasksList = taskViewModel.completeTasksList()
                        if (completeTasksList.isNotEmpty()) {
                            rvAchievements.apply {
                                layoutManager = LinearLayoutManager(context)
                                adapter = TasksAdapter(requireContext(), completeTasksList, ::markTask, true)
                            }
                            tvNoAchievements.visibility = View.GONE
                        }
                    }
                } else {
                    view?.let {
                        Snackbar.make(it, taskViewModel.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })

        // mark tasks as done
        taskViewModel.successfulUpdate.observe(viewLifecycleOwner, {
            successful ->
            if (successful != null) {
                if (successful) {
                    view?.let {
                        Snackbar.make(it, requireContext().getString(R.string.mark_task_success), Snackbar.LENGTH_LONG).show()
                    }
                    // get tasks again to refresh list
                    taskViewModel.getTasks(mentorshipRelation.id)
                } else {
                    view?.let {
                        Snackbar.make(it, taskViewModel.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })

        // add task
        taskViewModel.successfulAdd.observe(viewLifecycleOwner, {
            successful ->
            if (successful != null) {
                if (successful) {
                    view?.let {
                        Snackbar.make(it, requireContext().getString(R.string.create_task_success), Snackbar.LENGTH_LONG).show()
                    }
                    // get tasks again to refresh list
                    taskViewModel.getTasks(mentorshipRelation.id)
                } else {
                    view?.let {
                        Snackbar.make(it, taskViewModel.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
        taskViewModel.getTasks(mentorshipRelation.id)
    }

    /**
     * The function creates a dialog box through which new tasks can be added
     */
    private fun showCompleteDialog() {
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        builder.setTitle(appContext.getString(R.string.add_new_task))
        val dialogLayout = inflater.inflate(R.layout.dialog_add_task, null)
        builder.setView(dialogLayout)
        builder.setCancelable(false)

        builder.setPositiveButton(appContext.getString(R.string.save)) { _, _ ->

        }
        builder.setNegativeButton(appContext.getString(R.string.cancel)) { dialogInterface, i ->
            dialogInterface.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val addTask = dialogLayout.tilEtAddTask

            // validation
            if (addTask.editText?.text?.isNotEmpty()!! && addTask.editText?.text?.length!! > 2) {
                taskViewModel.addTask(mentorshipRelation.id, CreateTask(addTask.editText?.text.toString()))
                dialog.dismiss()
            } else {
                dialogLayout.tilEtAddTask.error = getString(R.string.task_valid_description)
            }
        }
    }

    private fun markTask(taskId: Int) {
        taskViewModel.updateTask(taskId, true, mentorshipRelation.id)
    }
}
