package com.ddangddangddang.android.feature.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.CategoryRequest
import com.ddangddangddang.data.model.request.DirectRegionRequest
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionViewModel(private val repository: AuctionRepository) : ViewModel() {
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

    fun setClosingDate(year: Int, month: Int, dayOfMonth: Int) {
        Log.d("test", "setClosingDate: $month")
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

    fun submitAuction() {
        val isValidInputs = judgeValidInputs()
        if (!isValidInputs) return

        viewModelScope.launch {
            when (val response = repository.registerAuction(createRequestModel())) {
                is ApiResponse.Success -> {
                    _event.value = RegisterAuctionEvent.SubmitResult(response.body.id)
                }
                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    private fun createRequestModel(): RegisterAuctionRequest {
        val imageUrl = imageUrl.value ?: ""
        val title = title.value ?: ""
        val category = category.value ?: ""
        val description = description.value ?: ""
        val startPrice = startPrice.value?.toInt() ?: 0
        val bidUnit = bidUnit.value?.toInt() ?: 0
        val closingTime = closingTime.value.toString() + ":00" // seconds
        val directRegion = directRegion.value ?: ""

        return RegisterAuctionRequest(
            listOf(imageUrl),
            title,
            CategoryRequest(category.split(" > ")[0], category.split(" > ")[1]),
            description,
            startPrice,
            bidUnit,
            closingTime,
            listOf(DirectRegionRequest("경기도", "부천시", "원미구")),
        )
    }

    private fun judgeValidInputs(): Boolean {
        val imageUrl = imageUrl.value
        val title = title.value
        val category = category.value
        val description = description.value
        val startPrice = startPrice.value
        val bidUnit = bidUnit.value
        val closingTime = closingTime.value
        val directRegion = directRegion.value

        if (imageUrl.isNullOrBlank() ||
            title.isNullOrBlank() ||
            category.isNullOrBlank() ||
            description.isNullOrBlank() ||
            startPrice.isNullOrBlank() ||
            bidUnit.isNullOrBlank() ||
            closingTime == null ||
            directRegion.isNullOrBlank()
        ) {
            setBlankExistEvent()
            return false
        }

        if (startPrice.toIntOrNull() == null || bidUnit.toIntOrNull() == null) {
            setInvalidValueInputEvent()
            return false
        }
        return true
    }

    private fun setBlankExistEvent() {
        _event.value = RegisterAuctionEvent.InputErrorEvent.BlankExistEvent
    }

    private fun setInvalidValueInputEvent() {
        _event.value = RegisterAuctionEvent.InputErrorEvent.InvalidValueInputEvent
    }

    sealed class RegisterAuctionEvent {
        object Exit : RegisterAuctionEvent()
        class ClosingTimePicker(val dateTime: LocalDateTime) : RegisterAuctionEvent()
        class SubmitResult(val id: Long) : RegisterAuctionEvent()
        sealed class InputErrorEvent : RegisterAuctionEvent() {
            object InvalidValueInputEvent : InputErrorEvent()
            object BlankExistEvent : InputErrorEvent()
        }
    }
}
