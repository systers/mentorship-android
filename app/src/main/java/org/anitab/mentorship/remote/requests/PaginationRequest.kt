package org.anitab.mentorship.remote.requests

/**
 * This data class represents all data necessary to paginate data
 * @param page the current page to be accessed
 * @param perPage data limit size for per page
 */
data class PaginationRequest(val page: Int, val perPage: Int) {
    val pagination = mapOf("page" to page.toString(), "per_page" to perPage.toString())
}
