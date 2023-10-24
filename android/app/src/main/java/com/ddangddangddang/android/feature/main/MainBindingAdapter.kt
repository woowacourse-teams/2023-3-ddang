package com.ddangddangddang.android.feature.main

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("onNavigationItemSelected")
fun BottomNavigationView.bindOnNavigationItemSelectedListener(
    onFragmentChange: (MainFragmentType) -> Unit,
) {
    this.setOnItemSelectedListener { menuItem ->
        val fragmentType = MainFragmentType.of(menuItem.itemId)
        onFragmentChange(fragmentType)
        true
    }
}
