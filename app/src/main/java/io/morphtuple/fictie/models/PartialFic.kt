package io.morphtuple.fictie.models

import androidx.recyclerview.widget.DiffUtil

data class PartialFic(
    val id: String,
    val title: String,
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

// TODO inherit an IDable, and use one diff callback
object PartialFicDiffCallback : DiffUtil.ItemCallback<PartialFic>() {
    override fun areItemsTheSame(oldItem: PartialFic, newItem: PartialFic): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: PartialFic, newItem: PartialFic): Boolean {
        return oldItem.title == newItem.title
    }
}