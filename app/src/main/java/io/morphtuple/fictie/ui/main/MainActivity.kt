package io.morphtuple.fictie.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.R
import io.morphtuple.fictie.ui.reader.ReaderActivity
import io.morphtuple.fictie.common.ui.FragmentBottomNavigationViewBinder
import io.morphtuple.fictie.databinding.ActivityMainBinding
import io.morphtuple.fictie.ui.library.LibraryFragment
import io.morphtuple.fictie.ui.search.SearchFragment
import io.morphtuple.fictie.ui.settings.SettingsFragment

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        FragmentBottomNavigationViewBinder().bind(
            supportFragmentManager,
            R.id.container,
            binding.mainBnv,
            listOf(R.id.menu_library, R.id.menu_search, R.id.menu_settings),
            listOf(LibraryFragment(), SearchFragment(), SettingsFragment()),
            true
        )

        if (intent?.data != null) {
            val intent = Intent(this, ReaderActivity::class.java).putExtra(
                ReaderActivity.EXTRA_FIC_ID,
                intent?.data?.lastPathSegment
            )

            startActivity(intent)
        }
    }
}
