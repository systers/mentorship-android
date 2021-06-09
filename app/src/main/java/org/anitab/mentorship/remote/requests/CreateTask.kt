package org.anitab.mentorship.remote.requests

import com.google.gson.annotations.SerializedName

/**
 * This data class represents all data necessary to create a comment
 * @param description the task description
 */
data class CreateTask(@SerializedName("description") val description: String)
