package org.anitab.mentorship.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * This data class represents a task related to a mentorship
 * relation.
 *
 * @param id The id of this task
 * @param description The description of this task
 * @param isDone Represents whether this task has been completed
 * @param createdAt Unix timestamp of when this task was created
 * @param completedAt Unix timestamp of when this task was completed
 */

@Parcelize
data class Task(
    val id: Int,
    val description: String,
    val createdAt: Float,
    val completedAt: Float?
) : Parcelable {
    val isDone: Boolean = completedAt != null
}
