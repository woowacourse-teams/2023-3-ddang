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
import com.ddangddangddang.android.util.binding.BindingActivity
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionActivity :
    BindingActivity<ActivityRegisterAuctionBinding>(R.layout.activity_register_auction) {
    private val viewModel by viewModels<RegisterAuctionViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.event.observe(
            this,
        ) {
            handleEvent(it)
        }
    }

    private fun handleEvent(event: RegisterAuctionViewModel.RegisterAuctionEvent) {
        when (event) {
            is RegisterAuctionViewModel.RegisterAuctionEvent.ClosingTimePicker -> {
                showDateTimePicker(event.dateTime)
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.Exit -> {
                backActivity()
            }

            is RegisterAuctionViewModel.RegisterAuctionEvent.Submit -> {
                // 값을 모두 가져와서 서버에게 주는 함수를 ViewModel에 작성하여 함수 실행
                finish()
            }
        }
    }

    private fun showDateTimePicker(selectedDateTime: LocalDateTime) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                viewModel.setClosingDate(year, month, dayOfMonth)
                showTimePicker(selectedDateTime.toLocalTime())
            },
            selectedDateTime.year,
            selectedDateTime.monthValue,
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

    private fun backActivity() {
        finish()
    }

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, RegisterAuctionActivity::class.java)
    }
}
