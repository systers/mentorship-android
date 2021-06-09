package org.anitab.mentorship.models

/**
 * This [Enum] represents all the states of a Mentorship Relation.
 * These values are the same used in the backend.
 * PENDING   - first applied state when a user sends a request
 * ACCEPTED  - when the receiving user accepts the relation, which will start at the time
 *             of acceptance
 * REJECTED  - when the receiving user rejects the relation, no longer available to be accepted
 * CANCELLED - when any of the users of a current relation cancel a relation
 * COMPLETED - when a current relation passes the end date it becomes completed,
 *             this happens automatically, does not require action from any user
 */
enum class RelationState(val value: Int) {
    PENDING(1),
    ACCEPTED(2),
    REJECTED(3),
    CANCELLED(4),
    COMPLETED(5)
}
