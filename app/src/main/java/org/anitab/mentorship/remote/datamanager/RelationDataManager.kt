package org.anitab.mentorship.remote.datamanager

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.remote.ApiManager
import org.anitab.mentorship.remote.requests.RelationshipRequest
import org.anitab.mentorship.remote.responses.CustomResponse

/**
 * This class represents the data manager related to Mentorship Relation API
 */
class RelationDataManager(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val apiManager = ApiManager.instance

    /**
     * This will call a method of RelationService interface to fetch
     * all mentorship requests and relations
     * @return an Observable of a list of [Relationship]
     */
    suspend fun getAllRelationsAndRequests(): List<Relationship> {
        return withContext(dispatcher) { apiManager.relationService.getAllRelationships() }
    }

    /**
     * This will call a method of RelationService interface to fetch
     * all pending mentorship requests and relations
     * @return an Observable of a list of [Relationship]s
     */
    suspend fun getAllPendingRelationsAndRequests(): List<Relationship> {
        return withContext(dispatcher) { apiManager.relationService.getAllPendingRelationships() }
    }

    /**
     * This will call a method of RelationService interface to fetch
     * past mentorship requests and relations
     * @return an Observable of a list of [Relationship]
     */
    suspend fun getPastRelationships(): List<Relationship> {
        return withContext(dispatcher) { apiManager.relationService.getPastRelationships() }
    }

    /**
     * This will call a method from RelationService interface to accept a mentorship request
     * @param relationId id of the request being accepted
     * @return an Observable of [CustomResponse]
     */
    suspend fun acceptRelationship(relationId: Int): CustomResponse {
        return withContext(dispatcher) { apiManager.relationService.acceptRelationship(relationId) }
    }

    /**
     * This will call a method from RelationService interface to reject a mentorship request
     * @param relationId id of the request being rejected
     * @return an Observable of [CustomResponse]
     */
    suspend fun rejectRelationship(relationId: Int): CustomResponse {
        return withContext(dispatcher) { apiManager.relationService.rejectRelationship(relationId) }
    }

    /**
     * This will call a method from RelationService interface to delete a mentorship request
     * @param relationId id of the request being deleted
     * @return an Observable of [CustomResponse]
     */
    suspend fun deleteRelationship(relationId: Int): CustomResponse {
        return withContext(dispatcher) { apiManager.relationService.deleteRelationship(relationId) }
    }

    /**
     * This will call a method from RelationService interface to cancel a mentorship relation
     * @param relationId id of the request being canceled
     * @return an Observable of [CustomResponse]
     */
    suspend fun cancelRelationship(relationId: Int): CustomResponse {
        return withContext(dispatcher) { apiManager.relationService.cancelRelationship(relationId) }
    }

    /**
     * This will call a method from RelationService interface to send mentorship request
     * @param relationshipRequest object with fields to send a mentorship request
     * @return an Observable of [CustomResponse]
     */
    suspend fun sendRequest(relationshipRequest: RelationshipRequest): CustomResponse {
        return withContext(dispatcher) { apiManager.relationService.sendRequest(relationshipRequest) }
    }

    /**
     * This will call a method from RelationService interface to get accepted mentorship relation
     * @return an Observable of [CustomResponse]
     */
    suspend fun getCurrentRelationship(): Relationship {
        return withContext(dispatcher) { apiManager.relationService.getCurrentRelationship() }
    }
}
