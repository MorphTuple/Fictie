package io.morphtuple.fictie.services

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import io.morphtuple.fictie.dao.BookmarkedFicDao
import io.morphtuple.fictie.models.Fic
import io.morphtuple.fictie.models.FicSearchQuery
import io.morphtuple.fictie.models.MarkedPartialFic
import io.morphtuple.fictie.models.PartialFic
import org.jsoup.Jsoup
import javax.inject.Inject

class AO3Service @Inject constructor(
    private val bookmarkedFicDao: BookmarkedFicDao
) {
    companion object {
        private const val AO3Endpoint = "https://archiveofourown.org/"
    }

    private fun String.parseIntOrNullComma(): Int? = replace(",", "").toIntOrNull(radix = 10)

    fun getFic(ficId: String): Fic? {
        val resp = Jsoup.connect("$AO3Endpoint/works/$ficId").cookie("view_adult", "true").get()
        val title = resp.select(".title.heading").first()?.text()
        val userStuff = resp.select(".userstuff").html()

        if (title == null) return null

        return Fic(title = title, userStuff = userStuff)
    }

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

    // TODO have separate bookmarks table with proper relations
    fun search(searchQuery: FicSearchQuery, pageIndex: Int): List<MarkedPartialFic> {
        // TODO better query string serialization
        val resp =
            Jsoup.connect(AO3Endpoint + "/works/search" + searchQuery.toQueryString() + "&page=" + pageIndex)
                .get()
        val list = resp.select(".work.index.group > li")

        return list.map {
            val title = it.select(".header.module > .heading > a").first()?.text().orEmpty()
            val fandoms = it.select(".fandoms.heading > a.tag").map { e -> e.text() }
            val tags = it.select(".tags.commas > li > a").map { e -> e.text() }
            val summary = it.select(".userstuff.summary").text().orEmpty()
            val ficId = it.select(".header.module > .heading > a").first()?.attr("href").orEmpty()
                .substringAfterLast("/")
            val author =
                it.select(".header.module > .heading > a[rel=\"author\"]").first()?.text().orEmpty()

            val language = it.select("dd.language").first()?.text().orEmpty()
            val wordCount = it.select("dd.words").first()?.text()?.parseIntOrNullComma()
            val chapters = it.select("dd.chapters").first()?.text().orEmpty()
            val commentCount =
                it.select("dd.comments > a").first()?.text()?.parseIntOrNullComma()
            val kudos = it.select("dd.kudos > a").first()?.text()?.parseIntOrNullComma()
            val bookmarkCount = it.select("dd.bookmarks > a").first()?.text()?.parseIntOrNullComma()
            val hits = it.select("dd.hits").first()?.text()?.parseIntOrNullComma()

            val requiredTags =
                it.select("ul.required-tags > li > a > span > .text").map { e -> e.text() }

            val rating = requiredTags[0].orEmpty()
            val warning = requiredTags[1].orEmpty()
            val category = requiredTags[2].orEmpty()
            val status = requiredTags[3].orEmpty()

            val bookmarked = bookmarkedFicDao.exists(ficId)

            MarkedPartialFic(
                PartialFic(
                    id = ficId,
                    title = title,
                    fandoms = fandoms,
                    tags = tags,
                    summary = summary,
                    author = author,

                    language = language,
                    wordCount = wordCount,
                    chapters = chapters,
                    commentCount = commentCount,
                    kudos = kudos,
                    bookmarkCount = bookmarkCount,
                    hitCount = hits,

                    rating = rating,
                    warning = warning,
                    category = category,
                    status = status
                ), bookmarked != null
            )
        }.toList()
    }
}