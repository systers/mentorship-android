package org.anitab.mentorship.remote.services

import org.anitab.mentorship.models.HomeStatistics
import org.anitab.mentorship.models.User
import org.anitab.mentorship.remote.requests.ChangePassword
import org.anitab.mentorship.remote.responses.CustomResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * This interface describes the methods related to Users REST API
 */
interface UserService {

    /**
     * This function returns all users of the system
     * @return an observable instance of a list of [User]s
     */
    @GET("users")
    suspend fun getUsers(): List<User>

    /**
     * This function returns all users, with email verified,
     * of the system with requested pagination info
     * @return an observable instance of a list of [User]s
     */
    @GET("users/verified")
    suspend fun getVerifiedUsers(@QueryMap pagination: Map<String, String>): List<User>

    /**
     * This function returns all users, with email verified, of the system
     * @return an observable instance of a list of [User]s
     */
    @GET("users/verified")
    suspend fun getVerifiedUsers(): List<User>

    /**
     * This function returns a user's public profile of the system
     * @return an observable instance of the [User]
     */
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): User

    /**
     * This function returns the current user profile
     * @return an observable instance of the [User]
     */
    @GET("user")
    suspend fun getUser(): User

    /**
     * This function updates the current user's profile
     * @return an observable instance of the [CustomResponse]
     */
    @PUT("user")
    suspend fun updateUser(@Body user: User): CustomResponse

    /**
     * This function updates the current user password
     * @return an observable instance of the [CustomResponse]
     */
    @PUT("user/change_password")
    suspend fun updatePassword(@Body changePassword: ChangePassword): CustomResponse

    /**
     * This function gets the current user's home screen statistics
     * @return an observable instance of [HomeStatistics]
     */
    @GET("home")
    suspend fun getHomeStats(): HomeStatistics
}
