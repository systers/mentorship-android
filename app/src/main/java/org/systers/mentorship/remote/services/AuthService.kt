package org.systers.mentorship.remote.services

import org.systers.mentorship.remote.requests.Login
import org.systers.mentorship.remote.requests.Register
import org.systers.mentorship.remote.responses.AuthToken
import org.systers.mentorship.remote.responses.CustomResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * This interface describes the methods related to Authentication REST API
 */
interface AuthService {

    /**
     * This function allows a user to login into the system
     * @param login data required to login a user
     * @return an instance of the [AuthToken]
     */
    @POST("login")
    suspend fun login(@Body login: Login): AuthToken

    /**
     * This function allows a user to sign up into the system
     * @param register data required to register a user
     * @return an instance of the [CustomResponse]
     */
    @POST("register")
    suspend fun register(@Body register: Register): CustomResponse
}
