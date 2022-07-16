package io.morphtuple.fictie.models

data class FicNavigation(
    val ficId: String,
    val chapters: List<FicIndex>
)

data class FicIndex(
    val index: Int,
    val chapterTitle: String,
    val ficId: String,
    val chapterId: String,
    val chapterDate: String,
)