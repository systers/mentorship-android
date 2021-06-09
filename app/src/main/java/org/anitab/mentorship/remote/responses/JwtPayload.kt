package org.anitab.mentorship.remote.responses

/**
 * Response encoded in JSON Web Token, holding the token information and the current's user identity
 */
data class JwtPayload(
    val identity: Int,
    val iat: Long,
    val nbf: Long,
    val jti: String,
    val exp: Long,
    val fresh: Boolean,
    val type: String
)
