package com.ddangddangddang.android.feature.imageDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityImageDetailBinding
import com.ddangddangddang.android.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailActivity :
    BindingActivity<ActivityImageDetailBinding>(R.layout.activity_image_detail) {
    private val viewModel: ImageDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        if (viewModel.images.value == null) viewModel.setImages(getImageUrls())
    }

    private fun getImageUrls(): List<String> {
        val images = intent.getStringArrayExtra(IMAGE_URL_KEY) ?: emptyArray()
        if (images.isEmpty()) notifyNotExistImages()
        return images.toList()
    }

    private fun notifyNotExistImages() {
        finish()
    }

    companion object {
        private const val IMAGE_URL_KEY = "image_url_key"
        fun getIntent(context: Context, images: List<String>): Intent {
            return Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(IMAGE_URL_KEY, images.toTypedArray())
            }
        }
    }
}
