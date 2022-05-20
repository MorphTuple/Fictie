package io.morphtuple.fictie.models

import androidx.recyclerview.widget.DiffUtil

data class Marked<T : IDable>(
    val data: T,
    val marked: Boolean
)

class MarkedDiffCallback<T : IDable> : DiffUtil.ItemCallback<Marked<T>>() {
    override fun areItemsTheSame(oldItem: Marked<T>, newItem: Marked<T>): Boolean {
        return oldItem.data.id == newItem.data.id
    }

    override fun areContentsTheSame(oldItem: Marked<T>, newItem: Marked<T>): Boolean {
        return oldItem.data.id == newItem.data.id
    }
}