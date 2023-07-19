package com.ddangddangddang.android.feature.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionViewModel : ViewModel() {
    val imageUrl: MutableLiveData<String> = MutableLiveData("")
    val title: MutableLiveData<String> = MutableLiveData("")
    val category: MutableLiveData<String> = MutableLiveData("전자기기 > 노트북")
    val description: MutableLiveData<String> = MutableLiveData("")
    val startPrice: MutableLiveData<String> = MutableLiveData("0")
    val bidUnit: MutableLiveData<String> = MutableLiveData("0")
    private var _closingTime: MutableLiveData<LocalDateTime> =
        MutableLiveData<LocalDateTime>(LocalDateTime.now())
    val closingTime: LiveData<LocalDateTime>
        get() = _closingTime
    val directRegion: MutableLiveData<String> = MutableLiveData("경기도 부천시")

    private val _event: SingleLiveEvent<RegisterAuctionEvent> = SingleLiveEvent()
    val event: LiveData<RegisterAuctionEvent>
        get() = _event

    fun setClosingTimeEvent() {
        _event.value =
            RegisterAuctionEvent.ClosingTimePicker(_closingTime.value ?: LocalDateTime.now())
    }

    fun setExitEvent() {
        _event.value = RegisterAuctionEvent.Exit
    }

    fun setSubmitEvent() {
        _event.value = RegisterAuctionEvent.Submit
    }

    fun setClosingDate(year: Int, month: Int, dayOfMonth: Int) {
        _closingTime.value =
            LocalDateTime.of(
                LocalDate.of(year, month, dayOfMonth),
                _closingTime.value?.toLocalTime() ?: LocalDateTime.now().toLocalTime(),
            )
    }

    fun setClosingTime(hourOfDay: Int, minute: Int) {
        _closingTime.value = LocalDateTime.of(
            _closingTime.value?.toLocalDate() ?: LocalDateTime.now().toLocalDate(),
            LocalTime.of(hourOfDay, minute),
        )
    }

    sealed class RegisterAuctionEvent {
        object Exit : RegisterAuctionEvent()
        class ClosingTimePicker(val dateTime: LocalDateTime) : RegisterAuctionEvent()
        object Submit : RegisterAuctionEvent()
    }
}
