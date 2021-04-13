package org.systers.mentorship.remote.datamanager

import io.reactivex.Observable
import org.systers.mentorship.models.Relationship
import org.systers.mentorship.remote.ApiManager
import org.systers.mentorship.remote.requests.RelationshipRequest
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.remote.services.RelationService
import javax.inject.Inject

/**
 * This class represents the data manager related to Mentorship Relation API
 */
class RelationDataManager @Inject constructor(val relationService: RelationService ) {

    /**
     * This will call a method of RelationService interface to fetch
     * all mentorship requests and relations
     * @return an Observable of a list of [Relationship]
     */
    fun getAllRelationsAndRequests(): Observable<List<Relationship>> {
        return relationService.getAllRelationships()
    }

    /**
     * This will call a method of RelationService interface to fetch
     * all pending mentorship requests and relations
     * @return an Observable of a list of [Relationship]s
     */
    fun getAllPendingRelationsAndRequests(): Observable<List<Relationship>> {
        return relationService.getAllPendingRelationships()
    }

    /**
     * This will call a method of RelationService interface to fetch
     * past mentorship requests and relations
     * @return an Observable of a list of [Relationship]
     */
    fun getPastRelationships(): Observable<List<Relationship>> {
        return relationService.getPastRelationships()
    }

    /**
     * This will call a method from RelationService interface to accept a mentorship request
     * @param relationId id of the request being accepted
     * @return an Observable of [CustomResponse]
     */
    fun acceptRelationship(relationId: Int): Observable<CustomResponse> {
        return relationService.acceptRelationship(relationId)
    }

    /**
     * This will call a method from RelationService interface to reject a mentorship request
     * @param relationId id of the request being rejected
     * @return an Observable of [CustomResponse]
     */
    fun rejectRelationship(relationId: Int): Observable<CustomResponse> {
        return relationService.rejectRelationship(relationId)
    }

    /**
     * This will call a method from RelationService interface to delete a mentorship request
     * @param relationId id of the request being deleted
     * @return an Observable of [CustomResponse]
     */
    fun deleteRelationship(relationId: Int): Observable<CustomResponse> {
        return relationService.deleteRelationship(relationId)
    }

    /**
     * This will call a method from RelationService interface to cancel a mentorship relation
     * @param relationId id of the request being canceled
     * @return an Observable of [CustomResponse]
     */
    fun cancelRelationship(relationId: Int): Observable<CustomResponse> {
        return relationService.cancelRelationship(relationId)
    }

    /**
     * This will call a method from RelationService interface to send mentorship request
     * @param relationshipRequest object with fields to send a mentorship request
     * @return an Observable of [CustomResponse]
     */
    fun sendRequest(relationshipRequest: RelationshipRequest): Observable<CustomResponse> {
        return relationService.sendRequest(relationshipRequest)
    }

    /**
     * This will call a method from RelationService interface to get accepted mentorship relation
     * @return an Observable of [CustomResponse]
     */
    fun getCurrentRelationship(): Observable<Relationship> {
        return relationService.getCurrentRelationship()
    }
}
