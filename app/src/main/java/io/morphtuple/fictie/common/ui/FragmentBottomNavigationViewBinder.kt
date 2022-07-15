package io.morphtuple.fictie.common.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentBottomNavigationViewBinder {
    var currentFragment: Fragment? = null

    fun bind(
        fragmentManager: FragmentManager,
        container: Int,
        bnv: BottomNavigationView,
        menuItems: List<Int>,
        fragments: List<Fragment>,
        initFirst: Boolean
    ) {
        bnv.setOnItemSelectedListener {
            val index = menuItems.indexOf(it.itemId)
            val fragment = fragments[index]
            val ft = fragmentManager.beginTransaction()

            if (currentFragment != null) ft.hide(currentFragment!!)

            if (fragment.isAdded) ft.show(fragment)
            else ft.add(container, fragment)

            currentFragment = fragment

            ft.commit()

            return@setOnItemSelectedListener true
        }

        if (initFirst) {
            currentFragment = fragments[0]
            fragmentManager.beginTransaction().replace(container, currentFragment!!).commit()
        }

    }
}