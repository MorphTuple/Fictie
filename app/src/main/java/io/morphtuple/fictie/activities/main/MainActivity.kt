package io.morphtuple.fictie.activities.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.R
import io.morphtuple.fictie.activities.reader.ReaderActivity
import io.morphtuple.fictie.common.ui.FragmentBottomNavigationViewBinder
import io.morphtuple.fictie.common.ui.ViewPagerBottomNavigationViewBinder
import io.morphtuple.fictie.databinding.ActivityMainBinding
import io.morphtuple.fictie.fragments.LibraryFragment
import io.morphtuple.fictie.fragments.SearchFragment
import io.morphtuple.fictie.fragments.SettingsFragment

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
