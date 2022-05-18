package io.morphtuple.fictie.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.activities.reader.ReaderActivity
import io.morphtuple.fictie.adapters.ListPartialFicResultAdapter
import io.morphtuple.fictie.databinding.FragmentLibraryBinding
import io.morphtuple.fictie.models.MarkedPartialFic
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding

    private val viewModel by viewModels<LibraryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater)

        binding.libraryRv.layoutManager = LinearLayoutManager(activity)
        val adapter = ListPartialFicResultAdapter({
            val intent = Intent(activity, ReaderActivity::class.java).putExtra(
                ReaderActivity.EXTRA_FIC_ID,
                it.partialFic.id
            )

            startActivity(intent)
        }, {
            viewModel.toggleBookmark(it.partialFic)
        })

        lifecycleScope.launch {
            viewModel.getLibraryLiveData()

            val liveData = viewModel.getLibraryLiveData()
            liveData.observe(viewLifecycleOwner) { listFic ->
                lifecycleScope.launch {
                    adapter.submitList(listFic.map { e -> MarkedPartialFic(e, true) })
                }
            }
        }


        binding.libraryRv.adapter = adapter

        return binding.root
    }
}