package com.ddangddangddang.android.global

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getBitmapFromUrl(context: Context, url: String): Bitmap {
    return withContext(Dispatchers.IO) {
        Glide.with(context.applicationContext)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    }
}
