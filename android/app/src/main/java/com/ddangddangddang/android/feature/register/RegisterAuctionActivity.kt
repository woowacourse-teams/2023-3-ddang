package com.ddangddangddang.android.feature.register

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityRegisterAuctionBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.feature.detail.AuctionDetailActivity
import com.ddangddangddang.android.util.binding.BindingActivity
import com.ddangddangddang.android.util.view.showDialog
import com.ddangddangddang.android.util.view.showSnackbar
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionActivity :
    BindingActivity<ActivityRegisterAuctionBinding>(R.layout.activity_register_auction) {
    private val viewModel by viewModels<RegisterAuctionViewModel> { viewModelFactory }
    private val imageAdapter = RegisterAuctionImageAdapter { viewModel.setDeleteImageEvent(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupViewModel()
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
                showDeleteImageDialog(event.imageUrl)
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

    private fun showDeleteImageDialog(imageUrl: String) {
        showDialog(
            messageId = R.string.register_auction_dialog_delete_image_message,
            positiveStringId = R.string.register_auction_dialog_delete_image_positive_button,
            actionPositive = { viewModel.deleteImage(imageUrl) },
        )
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
