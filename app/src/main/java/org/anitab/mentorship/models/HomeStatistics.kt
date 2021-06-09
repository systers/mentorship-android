package org.anitab.mentorship.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * This class represents statistics of the user actions on the app.
 * @param name the name of the user
 * @param pendingRequests number of pending requests
 * @param acceptedRequests number of accepted requests
 * @param completedRelations number of completed relations
 * @param cancelledRelations number of cancelled relations
 * @param rejectedRequests number of rejected requests
 * @param achievements a list of up-to 3 completed tasks
 */

@Parcelize
data class HomeStatistics(
    val name: String,
    val pendingRequests: Int,
    val acceptedRequests: Int,
    val completedRelations: Int,
    val cancelledRelations: Int,
    val rejectedRequests: Int,
    val achievements: List<Task>
) : Parcelable
