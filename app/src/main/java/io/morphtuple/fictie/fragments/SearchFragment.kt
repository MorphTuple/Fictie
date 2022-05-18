package io.morphtuple.fictie.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.activities.reader.ReaderActivity
import io.morphtuple.fictie.adapters.PagingPartialFicResultAdapter
import io.morphtuple.fictie.databinding.FragmentSearchBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
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

        val searchResultAdapter = PagingPartialFicResultAdapter({
            val intent = Intent(activity, ReaderActivity::class.java).putExtra(
                ReaderActivity.EXTRA_FIC_ID,
                it.partialFic.id
            )

            startActivity(intent)
        }, {
            viewModel.toggleBookmark(it.partialFic)
        })

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
