package org.anitab.mentorship.utils

import android.util.Base64
import android.util.Log
import org.anitab.mentorship.remote.responses.JwtPayload

/**
 * Decodes a JSON Web Token's header and body
 * @param jwt JSON Web Token in string format
 * @return body/payload of jwt Base64 decoded
 */
fun decodeJwtPayload(jwt: String): String {
    Log.d("JWT itself : ", jwt)
    val jwtParsed = jwt.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val base64EncodedBody = jwtParsed[1]
    val base64DecodedBody = Base64.decode(base64EncodedBody, Base64.DEFAULT)

    val body = String(base64DecodedBody)
    Log.d("JWT Body : ", body)

    return body
}

/**
 * Converts the JWT payload response from string format to [JwtPayload]
 * @param str JSON Web Token payload in string format
 * @return [JwtPayload] holding the token decoded
 */
fun convertJwtPayloadToObject(str: String): JwtPayload {
    return CommonUtils.gson.fromJson(str, JwtPayload::class.java)
}

/**
 * Converts the JWT token of the current user to [JwtPayload]
 * @return [JwtPayload] holding the current's user token decoded
 */
fun getAuthTokenPayload(): JwtPayload {
    val decodedJwtBody = decodeJwtPayload(PreferenceManager().authToken)
    return convertJwtPayloadToObject(decodedJwtBody)
}
