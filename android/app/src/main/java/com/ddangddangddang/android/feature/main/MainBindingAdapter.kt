package com.ddangddangddang.android.feature.main

import androidx.databinding.BindingAdapter
import com.ddangddangddang.android.R
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("onNavigationItemSelected")
fun BottomNavigationView.bindOnNavigationItemSelectedListener(
    onFragmentChange: (FragmentType) -> Unit,
) {
    this.setOnItemSelectedListener { menuItem ->
        val fragmentType = when (menuItem.itemId) {
            R.id.menu_item_home -> FragmentType.HOME
            R.id.menu_item_search -> FragmentType.SEARCH
            R.id.menu_item_message -> FragmentType.MESSAGE
            R.id.menu_item_my_page -> FragmentType.MY_PAGE
            else -> throw IllegalArgumentException("Not found menu item")
        }
        onFragmentChange(fragmentType)
        true
    }
}
