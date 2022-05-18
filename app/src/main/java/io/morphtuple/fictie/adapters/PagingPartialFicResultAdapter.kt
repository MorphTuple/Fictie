package io.morphtuple.fictie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import io.morphtuple.fictie.databinding.LayoutRowSearchFicBinding
import io.morphtuple.fictie.models.MarkedPartialFic
import io.morphtuple.fictie.models.MarkedPartialFicDiffCallback

class PagingPartialFicResultAdapter constructor(
    private val onClick: ((MarkedPartialFic) -> Unit),
    private val onBookmarkClicked: ((MarkedPartialFic) -> Unit)
) :
    PagingDataAdapter<MarkedPartialFic, PartialFicViewHolder>(
        MarkedPartialFicDiffCallback
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