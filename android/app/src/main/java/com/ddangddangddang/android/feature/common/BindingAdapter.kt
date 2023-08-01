package com.ddangddangddang.android.feature.common

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("imageUri")
fun ImageView.setImageUrl(uri: Uri?) {
    uri?.let {
        Glide.with(context)
            .load(it)
            .into(this)
    }
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("onCloseClick")
fun Chip.onCloseClick(onCloseClick: () -> Unit) {
    this.setOnCloseIconClickListener {
        onCloseClick.invoke()
    }
}
