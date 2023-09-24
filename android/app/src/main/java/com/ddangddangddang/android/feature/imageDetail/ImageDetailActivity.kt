package com.ddangddangddang.android.feature.imageDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityImageDetailBinding
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
        if (viewModel.images.value == null) viewModel.setImages(getImageUrls(), getImageFocus())
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.images.observe(this) { setupImages(it) }
        viewModel.focusPosition.observe(this) {
            binding.vpImageList.setCurrentItem(it, false)
        }
        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun handleEvent(event: ImageDetailViewModel.Event) {
        when (event) {
            ImageDetailViewModel.Event.Exit -> finish()
        }
    }

    private fun getImageUrls(): List<String> {
        val images = intent.getStringArrayExtra(IMAGE_URL_KEY) ?: emptyArray()
        if (images.isEmpty()) notifyNotExistImages()
        return images.toList()
    }

    private fun getImageFocus(): Int {
        return intent.getIntExtra(FOCUS_IMAGE_POSITION_KEY, 0)
    }

    private fun notifyNotExistImages() {
        Toaster.showShort(this, getString(R.string.image_detail_images_not_exist))
        finish()
    }

    private fun setupImages(images: List<String>) {
        binding.vpImageList.apply {
            offscreenPageLimit = 1
            adapter = ImageDetailAdapter(images)
        }

        TabLayoutMediator(binding.tlIndicator, binding.vpImageList) { _, _ -> }.attach()
    }

    companion object {
        private const val IMAGE_URL_KEY = "image_url_key"
        private const val FOCUS_IMAGE_POSITION_KEY = "focus_image_position_key"
        fun getIntent(context: Context, images: List<String>, focusPosition: Int): Intent {
            return Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(FOCUS_IMAGE_POSITION_KEY, focusPosition)
                putExtra(IMAGE_URL_KEY, images.toTypedArray())
            }
        }
    }
}
