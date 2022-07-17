package io.morphtuple.fictie.models

import org.jsoup.nodes.Document

enum class FicElementType {
    // <p>
    PARAGRAPH,

    // .chapter > h3
    CHAPTER,

    // <hr>
    DIVIDER,

    // p > img
    IMAGE
}

data class FicElement(
    val text: String?,
    val elType: FicElementType,
    val imgLink: String? = null
) {
    companion object {
        fun parseElementsFromJsoupDocument(dom: Document): List<FicElement> {
            val list = mutableListOf<FicElement>()

            val chapterTitleText = dom.select(".chapter > h3").first()?.text().orEmpty()

            list.add(FicElement(chapterTitleText, FicElementType.CHAPTER))

            dom.select(".userstuff.module").first()?.children()?.forEach {
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
    }
}