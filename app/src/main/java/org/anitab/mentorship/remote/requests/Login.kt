package org.anitab.mentorship.remote.requests

/**
 * This data class represents all data necessary to create a login
 * @param username represents username
 * @param password represents user password
 */
data class Login(
    var username: String,
    var password: String
)
