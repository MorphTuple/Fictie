package io.morphtuple.fictie.services

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import io.morphtuple.fictie.common.parseIntOrNullComma
import io.morphtuple.fictie.dao.BookmarkedFicDao
import io.morphtuple.fictie.models.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLEncoder
import javax.inject.Inject

class AO3Service @Inject constructor(
    private val bookmarkedFicDao: BookmarkedFicDao
) {
    companion object {
        private const val AO3Endpoint = "https://archiveofourown.org/"
    }

    fun getChapterElements(ficId: String, chapterId: String): List<FicElement> =
        FicElement.parseElementsFromJsoupDocument(soupAO3("works/$ficId/chapters/$chapterId"))

    fun getNavigation(ficId: String): FicNavigation =
        FicNavigation.parseFromDocument(ficId, soupAO3("works/$ficId/navigate"))

    fun getLibraryLiveData(): LiveData<List<PartialFic>> {
        return bookmarkedFicDao.loadAllPartialFic()
    }

    fun toggleBookmark(partialFic: PartialFic): Boolean {
        return try {
            bookmarkedFicDao.insertAll(partialFic)
            true
        } catch (e: SQLiteConstraintException) {
            bookmarkedFicDao.delete(partialFic)
            false
        }
    }

    private fun toQueryString(searchQuery: String): String {
        return "?work_search[query]=${URLEncoder.encode(searchQuery, "utf-8")}"
    }

    // TODO have separate bookmarks table with proper relations
    fun search(searchQuery: String, pageIndex: Int): List<Marked<PartialFic>> {
        // TODO better query string serialization
        val resp =
            soupAO3("works/search" + toQueryString(searchQuery) + "&page=" + pageIndex)

        val list = resp.select(".work.index.group > li")

        return list.map {
            val partialFic = PartialFic.parseFromEl(it)
            val bookmarked = bookmarkedFicDao.exists(partialFic.id)

            Marked(partialFic, bookmarked != null)
        }.toList()
    }

    private fun soupAO3(path: String): Document {
        return Jsoup.connect("$AO3Endpoint/$path").cookie("view_adult", "true").get()
    }
}