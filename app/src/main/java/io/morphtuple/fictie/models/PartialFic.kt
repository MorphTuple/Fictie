package io.morphtuple.fictie.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partial_fic")
data class PartialFic(
    @PrimaryKey val id: String,
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
)

data class MarkedPartialFic(
    val partialFic: PartialFic,
    val bookmarked: Boolean
)

// TODO inherit an IDable, and use one diff callback
object PartialFicDiffCallback : DiffUtil.ItemCallback<PartialFic>() {
    override fun areItemsTheSame(oldItem: PartialFic, newItem: PartialFic): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PartialFic, newItem: PartialFic): Boolean {
        return oldItem.id == newItem.id
    }
}

object MarkedPartialFicDiffCallback : DiffUtil.ItemCallback<MarkedPartialFic>() {
    override fun areItemsTheSame(oldItem: MarkedPartialFic, newItem: MarkedPartialFic): Boolean {
        return oldItem.partialFic.id == newItem.partialFic.id
    }

    override fun areContentsTheSame(oldItem: MarkedPartialFic, newItem: MarkedPartialFic): Boolean {
        return oldItem.partialFic.id == newItem.partialFic.id
    }
}