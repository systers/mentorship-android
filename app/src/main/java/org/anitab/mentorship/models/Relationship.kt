package org.anitab.mentorship.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * This data class represents partial information of user of the system.
 * This is used in APIs not directly related with Users, such as in responses
 * related to MentorshipRelation
 *
 * @param id identifier of the mentorship relation
 * @param actionUserId id of the user that sent the request for this mentorship relation
 * @param sentByMe indication if the current user was the action user
 * @param mentor user with mentor role in the relation
 * @param mentee user with mentee role in the relation
 * @param creationDate date of creation unix timestamp
 * @param acceptDate date of acceptance unix timestamp
 * @param startDate start date unix timestamp
 * @param endDate end date unix timestamp
 * @param state state of the relation (@link to RelationState)
 * @param notes notes related to the mentorship relation
 */
@Parcelize
data class Relationship(
    val id: Int,
    val actionUserId: Int,
    val sentByMe: Boolean,
    val mentor: RelationUserResponse,
    val mentee: RelationUserResponse,
    val creationDate: Float,
    val acceptDate: Float,
    val startDate: Float,
    val endDate: Float,
    val state: Int,
    val notes: String
) : Parcelable {
    /**
     * This data class represents partial information of user of the system.
     * This is used in APIs not directly related with Users, such as in responses
     * related to MentorshipRelation
     *
     * @param id identifier of the user
     * @param name name of the user
     */
    @Parcelize
    data class RelationUserResponse(
        val id: Int,
        val name: String
    ) : Parcelable
}
