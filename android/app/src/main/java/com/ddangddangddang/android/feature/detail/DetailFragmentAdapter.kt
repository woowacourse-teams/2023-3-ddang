package com.ddangddangddang.android.feature.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailFragmentAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifeCycle) {
    override fun getItemCount(): Int = DetailFragmentType.values().size
    override fun createFragment(position: Int): Fragment {
        return DetailFragmentType.getTypeFrom(position).create()
    }
}
