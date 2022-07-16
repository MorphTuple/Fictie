package io.morphtuple.fictie.services

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import io.morphtuple.fictie.dao.BookmarkedFicDao
import io.morphtuple.fictie.models.*
import org.jsoup.Jsoup
import java.net.URLEncoder
import javax.inject.Inject

class AO3Service @Inject constructor(
    private val bookmarkedFicDao: BookmarkedFicDao
) {
    companion object {
        private const val AO3Endpoint = "https://archiveofourown.org/"
    }

    private fun String.parseIntOrNullComma(): Int? = replace(",", "").toIntOrNull(radix = 10)

    fun getChapterElements(ficId: String, chapterId: String): List<FicElement> {
        val list = mutableListOf<FicElement>()

        val url = "$AO3Endpoint/works/$ficId/chapters/$chapterId"
        val resp = Jsoup.connect(url).cookie("view_adult", "true").get()

        val chapterTitleText = resp.select(".chapter > h3").first()?.text().orEmpty()

        list.add(FicElement(chapterTitleText, FicElementType.CHAPTER))

        resp.select(".userstuff.module").first()?.children()?.forEach {
            if (it.`is`("p")) {
                val imgCheck = it.select("img").first()

                if (imgCheck != null) {
                    list.add(FicElement("", FicElementType.IMAGE, imgCheck.attr("href")))
                } else {
                    list.add(FicElement(it.text(), FicElementType.PARAGRAPH))
                }
            } else if (it.`is`("hr")) list.add(FicElement(null, FicElementType.DIVIDER, null))
        }

        return list
    }

    fun getFic(ficId: String, chapterId: String? = null): FicPage? {
        var url = "$AO3Endpoint/works/$ficId"
        if (chapterId != null) url += "/chapters/$chapterId"

        val resp = Jsoup.connect(url).cookie("view_adult", "true").get()
        val title = resp.select(".title.heading").first()?.text()
        val userStuff = resp.select(".userstuff").html()

        if (title == null) return null

        val multiChapter = resp.select("#selected_id").first()

        return FicPage(
            title = title,
            userStuff = userStuff,
            isFicMultiChapter = multiChapter != null
        )
    }

    fun getNavigation(ficId: String): FicNavigation {
        val resp =
            Jsoup.connect("$AO3Endpoint/works/$ficId/navigate").cookie("view_adult", "true").get()
        val chapters = resp.select(".chapter.index.group > li").mapIndexed { idx, it ->
            val a = it.select("a").first()
            val href = a?.attr("href").orEmpty()
            val chapterId = href.substringAfterLast("/")
            val title = a?.text().orEmpty()
            val date = it.select(".datetime").first()?.text()
                ?.replace("(", "")
                ?.replace(")", "")
                .orEmpty()

            FicIndex(idx + 1, title, ficId, chapterId, date)
        }

        return FicNavigation(ficId = ficId, chapters)
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

    private fun toQueryString(searchQuery: String): String {
        return "?work_search[query]=${URLEncoder.encode(searchQuery, "utf-8")}"
    }

    // TODO have separate bookmarks table with proper relations
    fun search(searchQuery: String, pageIndex: Int): List<Marked<PartialFic>> {
        // TODO better query string serialization
        val resp =
            Jsoup.connect(AO3Endpoint + "/works/search" + toQueryString(searchQuery) + "&page=" + pageIndex)
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

            Marked(
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