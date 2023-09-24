package com.ddangddangddang.android.feature.imageDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityImageDetailBinding
import com.ddangddangddang.android.feature.detail.AuctionImageAdapter
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.Toaster
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailActivity :
    BindingActivity<ActivityImageDetailBinding>(R.layout.activity_image_detail) {
    private val viewModel: ImageDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        if (viewModel.images.value == null) viewModel.setImages(getImageUrls())
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.images.observe(this) { setupImages(it) }
    }

    private fun getImageUrls(): List<String> {
        val images = intent.getStringArrayExtra(IMAGE_URL_KEY) ?: emptyArray()
        if (images.isEmpty()) notifyNotExistImages()
        return images.toList()
    }

    private fun notifyNotExistImages() {
        Toaster.showShort(this, getString(R.string.image_detail_images_not_exist))
        finish()
    }

    private fun setupImages(images: List<String>) {
        binding.vpImageList.apply {
            offscreenPageLimit = 1
            adapter = AuctionImageAdapter(images)
        }

        TabLayoutMediator(binding.tlIndicator, binding.vpImageList) { _, _ -> }.attach()
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
