package org.anitab.mentorship.remote.requests

/**
 * This data class represents all data necessary to changing user password
 * @param currentPassword the current password of the user
 * @param newPassword new password which will replace the current password.
 */
data class ChangePassword(
    val currentPassword: String,
    val newPassword: String
)
