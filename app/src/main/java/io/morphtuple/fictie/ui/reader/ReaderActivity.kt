package io.morphtuple.fictie.ui.reader

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.R
import io.morphtuple.fictie.data.preferences.PreferencesHelper
import io.morphtuple.fictie.databinding.ActivityReaderBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReaderActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_FIC_ID = "fic_id"
    }

    private val viewModel by viewModels<ReaderViewModel>()

    private lateinit var binding: ActivityReaderBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reader_toolbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            cleanupAndFinish()
        }

        val sp = PreferencesHelper(applicationContext)

        supportActionBar?.title = "Loading..."

        val adapter = PagingFicElementAdapter()
        binding.readerRv.adapter = adapter
        binding.readerRv.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.navigation.filterNotNull().first {
                binding.toolbar.title = it.title
                binding.toolbar.subtitle = "by ${it.author}"
                true
            }

            viewModel.flow.collectLatest { pagingData ->
                adapter.submitData(
                    pagingData
                )
            }
        }

        viewModel.loadNavigation(intent.getStringExtra(EXTRA_FIC_ID)!!)

        // TODO better error handling
        viewModel.networkError.observe(this) {
            if (it) {
                Toast.makeText(applicationContext, "Error fetching data :(", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
    }

    private fun cleanupAndFinish(){
        finish()
    }
}