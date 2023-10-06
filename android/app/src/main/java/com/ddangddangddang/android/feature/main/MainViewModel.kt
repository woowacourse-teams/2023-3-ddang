package com.ddangddangddang.android.feature.main

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.R

class MainViewModel : ViewModel() {
    private val _currentFragmentType: MutableLiveData<FragmentType> =
        MutableLiveData(FragmentType.HOME)
    val currentFragmentType: LiveData<FragmentType>
        get() = _currentFragmentType

    fun setCurrentFragment(item: MenuItem): Boolean {
        val menuItemId = item.itemId
        val pageType = getPageType(menuItemId)
        changeCurrentFragmentType(pageType)

        return true
    }

    private fun getPageType(menuItemId: Int): FragmentType {
        return when (menuItemId) {
            R.id.menu_item_home -> FragmentType.HOME
            R.id.menu_item_search -> FragmentType.SEARCH
            R.id.menu_item_message -> FragmentType.MESSAGE
            R.id.menu_item_my_page -> FragmentType.MY_PAGE
            else -> throw IllegalArgumentException("Not found menu item")
        }
    }

    private fun changeCurrentFragmentType(fragmentType: FragmentType) {
        if (currentFragmentType.value == fragmentType) return

        _currentFragmentType.value = fragmentType
    }
}
