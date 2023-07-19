package com.ddangddangddang.android.feature.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AuctionSpaceItemDecoration(private val spanCount: Int, private val space: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount // 1부터 시작

        if (position < spanCount) outRect.top = space
        if (column == 0) {
            outRect.left = space
            outRect.right = space / 2
        } else {
            outRect.left = space / 2
            outRect.right = space
        }
        outRect.bottom = space
    }
}
