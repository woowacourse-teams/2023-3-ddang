package com.ddangddangddang.android.feature.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionViewModel : ViewModel() {
    private var closingTime: LocalDateTime = LocalDateTime.now()

    private val _event: SingleLiveEvent<Event.ShowDatePicker> = SingleLiveEvent()
    val event: LiveData<Event.ShowDatePicker>
        get() = _event

    fun onClickDatePicker() {
        _event.value = Event.ShowDatePicker(
            closingTime.year,
            closingTime.month.value,
            closingTime.dayOfMonth,
            closingTime.hour,
            closingTime.minute,
        )
    }

    fun setClosingDate(year: Int, month: Int, dayOfMonth: Int) {
        closingTime =
            LocalDateTime.of(LocalDate.of(year, month, dayOfMonth), closingTime.toLocalTime())
    }

    fun setClosingTime(hourOfDay: Int, minute: Int) {
        closingTime = LocalDateTime.of(closingTime.toLocalDate(), LocalTime.of(hourOfDay, minute))
    }

    sealed class Event {
        class ShowDatePicker(val year: Int, val month: Int, val dayOfMonth: Int, val hourOfDay: Int, val minute: Int) :
            Event()
    }
}
