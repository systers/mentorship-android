package org.anitab.mentorship.remote.responses

/**
 * This data class represents all data necessary to create an authentication token
 * @param accessToken represents an authentication token
 * @param accessExpiry represents the expiry timestamp
 */
data class AuthToken(
    var accessToken: String,
    var accessExpiry: Float
)
