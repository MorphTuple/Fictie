package io.morphtuple.fictie.common.ui

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView

object ViewPagerBottomNavigationViewBinder {
    fun bind(pager: ViewPager2, bnv: NavigationBarView, menuItems: List<Int>) {
        val oppositeMap = menuItems.mapIndexed { idx, el -> Pair(el, idx) }
            .associateBy { t -> t.first }

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bnv.selectedItemId = menuItems[position]
                super.onPageSelected(position)
            }
        })

        bnv.setOnItemSelectedListener {
            pager.currentItem = oppositeMap[it.itemId]!!.second
            return@setOnItemSelectedListener true
        }
    }
}