package com.ddangddangddang.android.feature.common

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}
