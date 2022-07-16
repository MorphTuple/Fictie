package io.morphtuple.fictie.models

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
)