package org.anitab.mentorship.remote.services

import org.anitab.mentorship.models.Relationship
import org.anitab.mentorship.remote.requests.RelationshipRequest
import org.anitab.mentorship.remote.responses.CustomResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * This interface describes the methods related to Mentorship Relation REST API
 */
interface RelationService {

    /**
     * This function returns all mentorship requests and relations of the current user
     * @return an observable instance of a list of [Relationship]s
     */
    @GET("mentorship_relations")
    suspend fun getAllRelationships(): List<Relationship>

    /**
     * this function returns all pending mentorship requests and relations of the current user
     * @return an observable instance of a list of [Relationship]s
     * */
    @GET("mentorship_relations/pending")
    suspend fun getAllPendingRelationships(): List<Relationship>

    /**
     * this function returns all pending mentorship requests and relations of the current user
     * This function returns past mentorship requests and relations of the current user
     * @return an observable instance of a list of [Relationship]s
     */
    @GET("mentorship_relations/past")
    suspend fun getPastRelationships(): List<Relationship>

    /**
     * This function performs the acceptance of a mentorship request
     * @return an observable instance of [CustomResponse] with a proper error or success message
     */
    @PUT("mentorship_relation/{relation_id}/accept")
    suspend fun acceptRelationship(@Path("relation_id") relationId: Int): CustomResponse

    /**
     * This function performs the rejection of a mentorship request
     * @return an observable instance of [CustomResponse] with a proper error or success message
     */
    @PUT("mentorship_relation/{relation_id}/reject")
    suspend fun rejectRelationship(@Path("relation_id") relationId: Int): CustomResponse

    /**
     * This function performs the deletion of a mentorship request
     * @return an observable instance of [CustomResponse] with a proper error or success message
     */
    @DELETE("mentorship_relation/{relation_id}")
    suspend fun deleteRelationship(@Path("relation_id") relationId: Int): CustomResponse

    /**
     * This function performs the cancellation of a mentorship relation
     * @return an observable instance of [CustomResponse] with a proper error or success message
     */
    @PUT("mentorship_relation/{relation_id}/cancel")
    suspend fun cancelRelationship(@Path("relation_id") relationId: Int): CustomResponse

    /**
     * This function performs sending a mentorship request
     * @param relationshipRequest data required to send a mentorship request
     * @return an observable instance of [CustomResponse] with a proper error or success message
     */
    @POST("mentorship_relation/send_request")
    suspend fun sendRequest(@Body relationshipRequest: RelationshipRequest): CustomResponse

    /**
     * This function return the current mentorship relation
     * @return an observable instance of [Relationship]
     */
    @GET("mentorship_relations/current")
    suspend fun getCurrentRelationship(): Relationship
}
