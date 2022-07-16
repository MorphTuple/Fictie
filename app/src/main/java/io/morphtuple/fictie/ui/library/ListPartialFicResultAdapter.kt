package io.morphtuple.fictie.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.morphtuple.fictie.ui.shared.PartialFicViewHolder
import io.morphtuple.fictie.databinding.LayoutRowSearchFicBinding
import io.morphtuple.fictie.models.*

class ListPartialFicResultAdapter constructor(
    private val onClick: ((Marked<PartialFic>) -> Unit),
    private val onBookmarkClicked: ((Marked<PartialFic>) -> Unit)
) :
    ListAdapter<Marked<PartialFic>, PartialFicViewHolder>(
        MarkedDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PartialFicViewHolder {
        val view = LayoutRowSearchFicBinding.inflate(
            LayoutInflater.from(
                parent.context
            )
        )

        return PartialFicViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: PartialFicViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.rootView.setOnClickListener {
            item?.let { onClick(it) }
        }

        holder.bind(item, onBookmarkClicked)
    }
}