package com.ddangddangddang.android.feature.register

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityRegisterAuctionBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.model.RegisterImageModel
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.showDialog
import com.ddangddangddang.android.util.view.showSnackbar
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionActivity :
    BindingActivity<ActivityRegisterAuctionBinding>(R.layout.activity_register_auction) {
    private val viewModel by viewModels<RegisterAuctionViewModel> { viewModelFactory }
    private val imageAdapter = RegisterAuctionImageAdapter { viewModel.setDeleteImageEvent(it) }
    private val pickMultipleMediaLaunchers = setupMultipleMediaLaunchers()

    private fun setupMultipleMediaLaunchers(): List<ActivityResultLauncher<PickVisualMediaRequest>> {
        return List(RegisterAuctionViewModel.MAXIMUM_IMAGE_SIZE) { index ->
            if (index == 0) {
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    if (uri != null) {
                        val image = RegisterImageModel(uri = uri)
                        viewModel.addImages(listOf(image))
                    }
                }
            } else {
                registerForActivityResult(
                    ActivityResultContracts.PickMultipleVisualMedia(index + 1),
                ) { uris ->
                    if (uris.isNotEmpty()) {
                        val images = uris.map { uri -> RegisterImageModel(uri = uri) }
                        viewModel.addImages(images)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupViewModel()
        setupLinearLayoutRegisterImage()
        setupImageRecyclerView()
    }

    private fun setupViewModel() {
        viewModel.images.observe(this) { imageAdapter.setImages(it) }
        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun handleEvent(event: RegisterAuctionViewModel.RegisterAuctionEvent) {
        when (event) {
            is RegisterAuctionViewModel.RegisterAuctionEvent.ClosingTimePicker -> {
                showDateTimePicker(event.dateTime)
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.Exit -> {
                backActivity()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.SubmitResult -> {
                navigationToDetail(event.id)
                finish()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.InputErrorEvent.InvalidValueInputEvent -> {
                showInvalidInputMessage()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.InputErrorEvent.BlankExistEvent -> {
                showBlankExistMessage()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.DeleteImage -> {
                showDeleteImageDialog(event.image)
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.MultipleMediaPicker -> {
                pickMultipleMediaLaunchers[viewModel.selectableImageSize - 1].launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }
        }
    }

    private fun showDateTimePicker(selectedDateTime: LocalDateTime) {
        // DataPicker Dialog에서 month는 0~11 범위
        // LocalDateTime에서 month는 1~12 범위
        // 따라서, 기본값을 세팅해줄 때는 -1, LocalDateTime에 저장할 때는 +1을 해주어야함
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                viewModel.setClosingDate(year, month + 1, dayOfMonth)
                showTimePicker(selectedDateTime.toLocalTime())
            },
            selectedDateTime.year,
            selectedDateTime.monthValue - 1,
            selectedDateTime.dayOfMonth,
        ).show()
    }

    private fun showTimePicker(selectedTime: LocalTime) {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                viewModel.setClosingTime(hourOfDay, minute)
            },
            selectedTime.hour,
            selectedTime.minute,
            false,
        ).show()
    }

    private fun showBlankExistMessage() {
        binding.root.showSnackbar(
            R.string.register_auction_blank_exist_error,
            R.string.register_auction_ok,
        )
    }

    private fun showInvalidInputMessage() {
        binding.root.showSnackbar(
            R.string.register_auction_invalid_input_error,
            R.string.register_auction_ok,
        )
    }

    private fun backActivity() {
        finish()
    }

    private fun navigationToDetail(id: Long) {
        startActivity(AuctionDetailActivity.getIntent(this, id))
    }

    private fun showDeleteImageDialog(image: RegisterImageModel) {
        showDialog(
            messageId = R.string.register_auction_dialog_delete_image_message,
            positiveStringId = R.string.register_auction_dialog_delete_image_positive_button,
            actionPositive = { viewModel.deleteImage(image) },
        )
    }

    private fun setupLinearLayoutRegisterImage() {
        binding.llRegisterImage.setOnClickListener {
            if (viewModel.selectableImageSize <= 0) {
                binding.llRegisterImage.showSnackbar(textId = R.string.register_autcion_snackbar_image_limit)
            } else {
                viewModel.pickImages()
            }
        }
    }

    private fun setupImageRecyclerView() {
        with(binding.rvImage) {
            adapter = imageAdapter
            addItemDecoration(RegisterAuctionImageSpaceItemDecoration(space = 24))
        }
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, RegisterAuctionActivity::class.java)
    }
}
