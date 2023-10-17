package com.ddangddangddang.android.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ddangddangddang.android.feature.onboarding.profile.ProfileSettingFragment

class OnBoardingFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = OnBoardingPageType.values().size

    override fun createFragment(position: Int): Fragment {
        return when (OnBoardingPageType.values()[position]) {
            OnBoardingPageType.ProfileSetting -> ProfileSettingFragment()
        }
    }
}
