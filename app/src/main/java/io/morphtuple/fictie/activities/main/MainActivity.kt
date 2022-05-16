package io.morphtuple.fictie.activities.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.morphtuple.fictie.R
import io.morphtuple.fictie.common.ui.ViewPagerBottomNavigationViewBinder
import io.morphtuple.fictie.databinding.ActivityMainBinding
import io.morphtuple.fictie.fragments.SearchFragment

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainPager.adapter = ScreenSlidePagerAdapter(this)
        ViewPagerBottomNavigationViewBinder.bind(binding.mainPager, binding.mainBnv, listOf(R.id.menu_library, R.id.menu_search, R.id.menu_settings))
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = SearchFragment()
    }
}
