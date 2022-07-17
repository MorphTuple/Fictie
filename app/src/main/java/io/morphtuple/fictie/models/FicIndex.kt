package io.morphtuple.fictie.models

import org.jsoup.nodes.Document

data class FicNavigation(
    val ficId: String,
    val title: String,
    val author: String,
    val chapters: List<FicIndex>,
) {
    companion object {
        fun parseFromDocument(ficId: String, dom: Document): FicNavigation {
            val title = dom.select(".works-navigate > .heading > a").first()?.text().orEmpty()
            val author = dom.select(".works-navigate > .heading > a[rel=\"author\"]").first()?.text().orEmpty()

            val chapters = dom.select(".chapter.index.group > li").mapIndexed { idx, it ->
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

            return FicNavigation(ficId = ficId, title, author, chapters)
        }
    }
}

data class FicIndex(
    val index: Int,
    val chapterTitle: String,
    val ficId: String,
    val chapterId: String,
    val chapterDate: String,
)