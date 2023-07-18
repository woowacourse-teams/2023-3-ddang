package com.ddangddangddang.android.feature.register

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ActivityRegisterAuctionBinding
import com.ddangddangddang.android.feature.common.viewModelFactory
import com.ddangddangddang.android.util.binding.BindingActivity

class RegisterAuctionActivity :
    BindingActivity<ActivityRegisterAuctionBinding>(R.layout.activity_register_auction) {
    private val viewModel by viewModels<RegisterAuctionViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        viewModel.event.observe(
            this,
            Observer {
                showDateTimePicker(it.year, it.month, it.dayOfMonth, it.hourOfDay, it.minute)
            },
        )
    }

    private fun showDateTimePicker(
        selectedYear: Int,
        selectedMonth: Int,
        selectedDay: Int,
        selectedHour: Int,
        selectedMinute: Int,
    ) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                viewModel.setClosingDate(year, month, dayOfMonth)
                showTimePicker(selectedHour, selectedMinute)
            },
            selectedYear,
            selectedMonth,
            selectedDay,
        ).show()
    }

    private fun showTimePicker(selectedHour: Int, selectedMinute: Int) {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                viewModel.setClosingTime(hourOfDay, minute)
            },
            selectedHour,
            selectedMinute,
            false,
        ).show()
    }
}
