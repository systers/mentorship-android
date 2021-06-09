package org.anitab.mentorship.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.achievement_list_item.view.*
import org.anitab.mentorship.R
import org.anitab.mentorship.models.Task

/**
 * This class is the RecyclerView adapter for achievements. It is a subclass of [ListAdapter] for
 * easy async calculation of diffs using DiffUtil to provide nice animations when the data set
 * changes.
 */
class AchievementsAdapter : ListAdapter<Task, AchievementsAdapter.ViewHolder>(AchievementsItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.achievement_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * This class holds a view for each item of the Achievements list
     * @param itemView represents each view of achievements list
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * This function binds the description of the achievement to the textview
         * @param task The Achievement whose description is to be bound
         */
        fun bind(task: Task) {
            itemView.tvDescription.text = task.description
        }
    }
}

/**
 * This class represents the DiffCallback for tasks, and is used in [AchievementsAdapter]
 */
class AchievementsItemCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
}
