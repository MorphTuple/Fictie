package io.morphtuple.fictie.models

import java.net.URLEncoder

data class FicSearchQuery(
    val anyField: String?
) {
    fun toQueryString(): String {
        return "?work_search[query]=${URLEncoder.encode(anyField, "utf-8")}"
    }
}