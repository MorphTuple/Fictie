package io.morphtuple.fictie.services

import io.morphtuple.fictie.models.FicSearchQuery
import io.morphtuple.fictie.models.PartialFic
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class AO3Service {
    companion object {
        private const val AO3Endpoint = "https://archiveofourown.org/"
    }

    fun String.parseIntOrNullComma(): Int? = replace(",", "").toIntOrNull(radix = 10)

    fun search(searchQuery: FicSearchQuery): List<PartialFic> {
        val resp = Jsoup.connect(AO3Endpoint + "/works/search" + searchQuery.toQueryString()).get()
        val list = resp.select(".work.index.group > li")

        return list.map {
            val title = it.select(".header.module > .heading > a").first()?.text().orEmpty()
            val fandoms = it.select(".fandoms.heading > a.tag").map { e -> e.text() }
            val tags = it.select(".tags.commas > li > a").map { e -> e.text() }
            val summary = it.select(".userstuff.summary").text().orEmpty()
            val ficId = it.select(".header.module > .heading > a").first()?.attr("href").orEmpty()
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
                hitCount = hits
            )
        }.toList()
    }
}