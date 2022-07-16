package io.morphtuple.fictie.ui.reader

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.morphtuple.fictie.databinding.LayoutFicElementChapterBinding
import io.morphtuple.fictie.databinding.LayoutFicElementDividerBinding
import io.morphtuple.fictie.databinding.LayoutFicElementTextBinding
import io.morphtuple.fictie.models.FicElement

abstract class BaseFicElementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(ficElement: FicElement)
}

class FicChapterViewHolder(val view: LayoutFicElementChapterBinding) :
    BaseFicElementViewHolder(view.root) {

    override fun bind(ficElement: FicElement) {
        view.paragraph.text = ficElement.text
    }
}

class FicParagraphViewHolder(val view: LayoutFicElementTextBinding) :
    BaseFicElementViewHolder(view.root) {

    override fun bind(ficElement: FicElement) {
        view.paragraph.text = ficElement.text
    }
}

class FicDividerViewHolder(val view: LayoutFicElementDividerBinding) :
    BaseFicElementViewHolder(view.root) {
    override fun bind(ficElement: FicElement) {
    }
}

