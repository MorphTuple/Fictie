package io.morphtuple.fictie.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.databinding.FragmentSearchBinding
import io.morphtuple.fictie.databinding.LayoutRowSearchFicBinding
import io.morphtuple.fictie.models.PartialFic
import io.morphtuple.fictie.models.PartialFicDiffCallback
import io.morphtuple.fictie.toCommaString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment() : Fragment() {
    private val viewModel by viewModels<SearchViewModel>()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)

        binding.advancedSearchBtn.setOnClickListener {
            binding.advancedOptionsLayout.toggle()
        }

        val searchResultAdapter = FicSearchResultAdapter()
        binding.searchResultRv.layoutManager = LinearLayoutManager(activity)
        binding.searchResultRv.adapter = searchResultAdapter
        binding.searchResultRv.addItemDecoration(
            DividerItemDecoration(
                activity,
                GridLayoutManager.VERTICAL
            )
        )

        lifecycleScope.launch {
            viewModel.searchFlow.collectLatest { pagingData ->
                searchResultAdapter.submitData(
                    pagingData
                )
            }
        }

        val searchFunc = fun() {
            viewModel.anyField.value = binding.anyFieldEt.text.toString()
        }

        binding.searchBtn.setOnClickListener {
            searchFunc()
        }

        binding.anyFieldEt.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH)
                searchFunc()
            return@setOnEditorActionListener true
        }

        return binding.root
    }
}

class FicSearchResultAdapter :
    PagingDataAdapter<PartialFic, FicSearchResultAdapter.FicSearchResultViewHolder>(
        PartialFicDiffCallback
    ) {
    class FicSearchResultViewHolder constructor(private var binding: LayoutRowSearchFicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(partialFic: PartialFic?) {
            if (partialFic == null) return
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
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FicSearchResultViewHolder {
        return FicSearchResultViewHolder(
            LayoutRowSearchFicBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: FicSearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}