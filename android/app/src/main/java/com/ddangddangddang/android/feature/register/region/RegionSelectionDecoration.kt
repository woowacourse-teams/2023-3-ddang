package com.ddangddangddang.android.feature.register.region

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RegionSelectionDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) outRect.left = DEFAULT_ITEM_MARGIN
        outRect.right = DEFAULT_ITEM_MARGIN
    }

    companion object {
        private const val DEFAULT_ITEM_MARGIN = 42
    }
}
