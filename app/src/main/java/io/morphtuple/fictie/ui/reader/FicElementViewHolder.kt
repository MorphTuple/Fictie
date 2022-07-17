package io.morphtuple.fictie.ui.reader

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import io.morphtuple.fictie.R
import io.morphtuple.fictie.databinding.LayoutFicElementChapterBinding
import io.morphtuple.fictie.databinding.LayoutFicElementDividerBinding
import io.morphtuple.fictie.databinding.LayoutFicElementImageBinding
import io.morphtuple.fictie.databinding.LayoutFicElementTextBinding
import io.morphtuple.fictie.models.FicElement
import com.bumptech.glide.request.target.Target
import com.stfalcon.imageviewer.StfalconImageViewer

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

class FicImageViewHolder(val context: Context, val binding: LayoutFicElementImageBinding) :
    BaseFicElementViewHolder(binding.root) {

    override fun bind(ficElement: FicElement) {
        Glide
            .with(binding.root)
            .load(ficElement.imgLink)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    //TODO: something on exception
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imgLoadingIndicator.visibility = View.GONE
                    return false
                }
            })
            .into(binding.image)

        binding.image.setOnClickListener {
            StfalconImageViewer.Builder<String>(
                context,
                arrayListOf(ficElement.imgLink)
            ) { view, image ->
                Glide
                    .with(view)
                    .load(image)
                    .into(view)
            }.show()
        }
    }
}

