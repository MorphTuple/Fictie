package io.morphtuple.fictie.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.R
import io.morphtuple.fictie.databinding.FragmentSearchBinding
import io.morphtuple.fictie.databinding.LayoutRowSearchFicBinding
import io.morphtuple.fictie.models.PartialFic
import io.morphtuple.fictie.models.PartialFicDiffCallback

@AndroidEntryPoint
class SearchFragment() : Fragment() {
    private val viewModel by viewModels<SearchViewModel>()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)

        binding.advancedSearchBtn.setOnClickListener {
            binding.advancedOptionsLayout.toggle()
        }

        binding.searchBtn.setOnClickListener {
            viewModel.search(binding.anyFieldEt.text.toString())
        }

        val searchResultAdapter = FicSearchResultAdapter()
        binding.searchResultRv.layoutManager = LinearLayoutManager(activity)
        binding.searchResultRv.adapter = searchResultAdapter

        viewModel.liveSearchResult.observe(viewLifecycleOwner) {
            if (it != null) searchResultAdapter.submitList(it)
        }

        return binding.root
    }
}

class FicSearchResultAdapter :
    ListAdapter<PartialFic, FicSearchResultAdapter.FicSearchResultViewHolder>(
        PartialFicDiffCallback
    ) {
    class FicSearchResultViewHolder constructor(private var binding: LayoutRowSearchFicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(partialFic: PartialFic) {
            binding.titleTv.text = partialFic.title
            binding.authorTv.text = partialFic.author
            binding.tagsTv.text = partialFic.tags.joinToString(", ")
            binding.fandomsTv.text = partialFic.fandoms.joinToString(", ")
            binding.summaryTv.text = partialFic.summary
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