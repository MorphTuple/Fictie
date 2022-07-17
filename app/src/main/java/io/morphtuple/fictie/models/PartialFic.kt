package io.morphtuple.fictie.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.morphtuple.fictie.common.parseIntOrNullComma
import org.jsoup.nodes.Element

@Entity(tableName = "partial_fic")
data class PartialFic(
    @PrimaryKey override val id: String,
    val title: String,
    // TODO proper relationship storing, so it can be queried by tags/fandoms in Room
    val fandoms: List<String>,
    val tags: List<String>,
    val summary: String,
    val author: String,

    val language: String,
    val wordCount: Int?,
    val chapters: String,
    val commentCount: Int?,
    val kudos: Int?,
    val bookmarkCount: Int?,
    val hitCount: Int?,

    val rating: String,
    val warning: String,
    val category: String,
    val status: String
) : IDable {
    companion object {
        fun parseFromEl(dom: Element): PartialFic {
            val title = dom.select(".header.module > .heading > a").first()?.text().orEmpty()
            val fandoms = dom.select(".fandoms.heading > a.tag").map { e -> e.text() }
            val tags = dom.select(".tags.commas > li > a").map { e -> e.text() }
            val summary = dom.select(".userstuff.summary").text().orEmpty()
            val ficId = dom.select(".header.module > .heading > a").first()?.attr("href").orEmpty()
                .substringAfterLast("/")
            val author =
                dom.select(".header.module > .heading > a[rel=\"author\"]").first()?.text().orEmpty()

            val language = dom.select("dd.language").first()?.text().orEmpty()
            val wordCount = dom.select("dd.words").first()?.text()?.parseIntOrNullComma()
            val chapters = dom.select("dd.chapters").first()?.text().orEmpty()
            val commentCount =
                dom.select("dd.comments > a").first()?.text()?.parseIntOrNullComma()
            val kudos = dom.select("dd.kudos > a").first()?.text()?.parseIntOrNullComma()
            val bookmarkCount = dom.select("dd.bookmarks > a").first()?.text()?.parseIntOrNullComma()
            val hits = dom.select("dd.hits").first()?.text()?.parseIntOrNullComma()

            val requiredTags =
                dom.select("ul.required-tags > li > a > span > .text").map { e -> e.text() }

            val rating = requiredTags[0].orEmpty()
            val warning = requiredTags[1].orEmpty()
            val category = requiredTags[2].orEmpty()
            val status = requiredTags[3].orEmpty()

            return PartialFic(
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
            )
        }
    }
}

