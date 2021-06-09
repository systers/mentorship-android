package org.anitab.mentorship.remote.requests

/**
 * This data class represents all data necessary to register a new user
 * @param name represents the name of the new user
 * @param username represents the username of the new user, used for login
 * @param email represents the email of the new user, used for login
 * @param password represents the password of the new user, used for login
 * @param termsAndConditionsChecked is true if the user checked the terms and
 *                                      conditions checkbox, false if otherwise
 * @param needMentoring is true if user wants to be mentored
 * @param availableToMentor is true if user is available as mentor

 */
data class Register(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val termsAndConditionsChecked: Boolean,
    val needMentoring: Boolean,
    val availableToMentor: Boolean
)
