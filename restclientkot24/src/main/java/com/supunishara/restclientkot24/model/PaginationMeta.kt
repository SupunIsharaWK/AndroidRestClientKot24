package com.supunishara.restclientkot24.model

/**
 * Metadata for paginated API responses.
 *
 * @param currentPage The current page number.
 * @param pageSize The number of items per page.
 * @param totalPages The total number of pages available.
 * @param totalItems The total number of items across all pages.
 */
data class PaginationMeta(
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Int
)