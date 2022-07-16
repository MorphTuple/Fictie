package io.morphtuple.fictie.ui.reader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import io.morphtuple.fictie.common.HashDiffCallback
import io.morphtuple.fictie.databinding.LayoutFicElementChapterBinding
import io.morphtuple.fictie.databinding.LayoutFicElementDividerBinding
import io.morphtuple.fictie.databinding.LayoutFicElementTextBinding
import io.morphtuple.fictie.models.FicElement
import io.morphtuple.fictie.models.FicElementType

class PagingFicElementAdapter :
    PagingDataAdapter<FicElement, BaseFicElementViewHolder>(HashDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.elType) {
            FicElementType.CHAPTER -> 0
            FicElementType.PARAGRAPH -> 1
            FicElementType.DIVIDER -> 2
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: BaseFicElementViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFicElementViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            0 -> FicChapterViewHolder(LayoutFicElementChapterBinding.inflate(inflater))
            1 -> FicParagraphViewHolder(LayoutFicElementTextBinding.inflate(inflater))
            else -> FicDividerViewHolder(LayoutFicElementDividerBinding.inflate(inflater))
        }
    }
}