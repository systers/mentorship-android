package org.anitab.mentorship.remote.datamanager

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.anitab.mentorship.remote.ApiManager
import org.anitab.mentorship.remote.requests.Login
import org.anitab.mentorship.remote.requests.Register
import org.anitab.mentorship.remote.responses.AuthToken
import org.anitab.mentorship.remote.responses.CustomResponse

/**
 * This class represents the data manager related to Authentication API
 */
class AuthDataManager(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val apiManager = ApiManager.instance

    /**
     * This will call the login method of AuthService interface
     * @param login The login request body containing the credentials
     * @return an Observable AuthToken
     */
    suspend fun login(login: Login): AuthToken {
        return withContext(dispatcher) {
            apiManager.authService.login(login)
        }
    }

    /**
     * This will call the register method of AuthService interface
     * @param register The registration request body containing
     *                        the registration required fields
     * @return an Observable CustomResponse
     */
    suspend fun register(register: Register): CustomResponse {
        return withContext(dispatcher) { apiManager.authService.register(register) }
    }
}
