package org.anitab.mentorship.remote.datamanager

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.anitab.mentorship.models.HomeStatistics
import org.anitab.mentorship.models.User
import org.anitab.mentorship.remote.ApiManager
import org.anitab.mentorship.remote.requests.ChangePassword
import org.anitab.mentorship.remote.requests.PaginationRequest
import org.anitab.mentorship.remote.responses.CustomResponse

/**
 * This class represents the data manager related to Users API
 */
class UserDataManager(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val apiManager = ApiManager.instance

    /**
     * This will call the getVerifiedUsers method of UserService interface
     * @return an Observable of a list of [User]
     */
    suspend fun getUsers(): List<User> {
        return withContext(dispatcher) { apiManager.userService.getVerifiedUsers() }
    }

    /**
     * This will call the getVerifiedUsers(pagination) method of UserService interface
     * @return an Observable of a list of [User]
     */
    suspend fun getUsers(paginationRequest: PaginationRequest): List<User> {
        return withContext(dispatcher) { apiManager.userService.getVerifiedUsers(paginationRequest.pagination) }
    }

    /**
     * This will call the getUser method of UserService interface
     * @return an Observable of [User]
     */
    suspend fun getUser(userId: Int): User {
        return withContext(dispatcher) { apiManager.userService.getUser(userId) }
    }

    /**
     * This will call the getUser method of UserService interface
     * @return an Observable of [User]
     */
    suspend fun getUser(): User {
        return withContext(dispatcher) { apiManager.userService.getUser() }
    }

    /**
     * This will call the updateUser method of UserService interface
     * @return an Observable of [CustomResponse]
     */
    suspend fun updateUser(user: User): CustomResponse {
        return withContext(dispatcher) { apiManager.userService.updateUser(user) }
    }

    /**
     * This will call the updatePassword method of UserService interface
     * @return an Observable of [CustomResponse]
     */
    suspend fun updatePassword(changePassword: ChangePassword): CustomResponse {
        return withContext(dispatcher) { apiManager.userService.updatePassword(changePassword) }
    }

    /**
     * This function fetches user statistics
     * @return an observable of [HomeStatistics]
     */
    suspend fun getHomeStats(): HomeStatistics {
        return withContext(dispatcher) { apiManager.userService.getHomeStats() }
    }
}
