package io.morphtuple.fictie.adapters

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.morphtuple.fictie.R
import io.morphtuple.fictie.databinding.LayoutRowSearchFicBinding
import io.morphtuple.fictie.models.Marked
import io.morphtuple.fictie.models.PartialFic
import io.morphtuple.fictie.toCommaString

class PartialFicViewHolder constructor(private var binding: LayoutRowSearchFicBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(
        markedPartialFic: Marked<PartialFic>?,
        onBookmarkClicked: (Marked<PartialFic>) -> Unit
    ) {
        if (markedPartialFic == null) return
        val partialFic = markedPartialFic.data
        var bookmarked = markedPartialFic.marked

        binding.titleTv.text = partialFic.title
        binding.authorTv.text = partialFic.author
        binding.tagsTv.text = partialFic.tags.joinToString(", ")
        binding.fandomsTv.text = partialFic.fandoms.joinToString(", ")
        binding.summaryTv.text = partialFic.summary
        binding.chaptersTv.text = "🚂 ${partialFic.chapters}"

        binding.languageTv.text = partialFic.language
        binding.wordsTv.visibility =
            if (partialFic.wordCount == null) View.GONE else View.VISIBLE
        binding.commentCountTv.visibility =
            if (partialFic.commentCount == null) View.GONE else View.VISIBLE
        binding.kudosCountTv.visibility =
            if (partialFic.kudos == null) View.GONE else View.VISIBLE
        binding.hitsTv.visibility =
            if (partialFic.hitCount == null) View.GONE else View.VISIBLE
        binding.bookmarkCountTv.visibility =
            if (partialFic.bookmarkCount == null) View.GONE else View.VISIBLE

        binding.wordsTv.text = "🔤 ${partialFic.wordCount.toCommaString()}"
        binding.commentCountTv.text = "💭 ${partialFic.commentCount.toCommaString()}"
        binding.kudosCountTv.text = "💖 ${partialFic.kudos.toCommaString()}"
        binding.bookmarkCountTv.text = "📗 ${partialFic.bookmarkCount.toCommaString()}"
        binding.hitsTv.text = "👀 ${partialFic.hitCount.toCommaString()}"

        binding.ratingTv.text = partialFic.rating
        binding.categoryTv.text = partialFic.category
        binding.warningTv.text = partialFic.warning
        binding.statusTv.text = partialFic.status

        val triggerBookmarked = fun(){
            binding.bookmarkBtn.setImageResource(
                if (bookmarked)
                    R.drawable.baseline_bookmark_black_24dp else
                    R.drawable.baseline_bookmark_border_black_24dp
            )
        }

        triggerBookmarked()

        binding.bookmarkBtn.setOnClickListener {
            onBookmarkClicked(markedPartialFic)
            bookmarked = !bookmarked
            triggerBookmarked()
        }
    }
}