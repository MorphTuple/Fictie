package io.morphtuple.fictie.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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
) : IDable

