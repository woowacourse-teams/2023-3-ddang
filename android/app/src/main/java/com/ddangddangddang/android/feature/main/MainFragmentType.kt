package com.ddangddangddang.android.feature.main

import androidx.annotation.IdRes
import com.ddangddangddang.android.R

enum class MainFragmentType(@IdRes val id: Int, val tag: String) {
    HOME(R.id.menu_item_home, "fragment_home_tag"),
    SEARCH(R.id.menu_item_search, "fragment_search_tag"),
    MESSAGE(R.id.menu_item_message, "fragment_message_tag"),
    MY_PAGE(R.id.menu_item_my_page, "fragment_my_page_tag"), ;

    companion object {
        fun of(@IdRes menuId: Int): MainFragmentType {
            return values().firstOrNull { it.id == menuId }
                ?: throw IllegalArgumentException("No match type for menu item")
        }
    }
}
