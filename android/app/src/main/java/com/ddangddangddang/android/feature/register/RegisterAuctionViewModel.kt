package com.ddangddangddang.android.feature.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.android.model.RegionSelectionModel
import com.ddangddangddang.android.model.RegisterImageModel
import com.ddangddangddang.android.util.image.toAdjustImageFile
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RegisterAuctionViewModel(private val repository: AuctionRepository) : ViewModel() {
    // EditText Values - Two Way Binding
    val title: MutableLiveData<String> = MutableLiveData("")
    val description: MutableLiveData<String> = MutableLiveData("")
    val startPrice: MutableLiveData<String> = MutableLiveData()
    val bidUnit: MutableLiveData<String> = MutableLiveData()

    // Images
    private val _images: MutableLiveData<List<RegisterImageModel>> = MutableLiveData()
    val images: LiveData<List<RegisterImageModel>>
        get() = _images
    val selectableImageSize: Int
        get() = MAXIMUM_IMAGE_SIZE - (images.value?.size ?: 0)

    // Category
    private val _category: MutableLiveData<CategoryModel> = MutableLiveData()
    val category: LiveData<String>
        get() = _category.map { it.name }

    // DateTime
    private var _closingTime: MutableLiveData<LocalDateTime> =
        MutableLiveData<LocalDateTime>(LocalDateTime.now())
    val closingTime: LiveData<LocalDateTime>
        get() = _closingTime

    // Direct Region
    private val _directRegion: MutableLiveData<List<RegionSelectionModel>> = MutableLiveData()
    val directRegion: LiveData<String>
        get() = _directRegion.map { regions -> regions.joinToString { it.name } }

    // Event
    private val _event: SingleLiveEvent<RegisterAuctionEvent> = SingleLiveEvent()
    val event: LiveData<RegisterAuctionEvent>
        get() = _event

    private var isLoading: Boolean = false

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

    fun submitAuction(context: Context) {
        if (isLoading) return
        val isValidInputs = judgeValidInputs()
        if (!isValidInputs) return

        isLoading = true
        viewModelScope.launch {
            val files =
                images.value?.mapNotNull { it.uri.toAdjustImageFile(context) } ?: emptyList()
            when (val response = repository.registerAuction(files, createRequestModel())) {
                is ApiResponse.Success -> {
                    _event.value = RegisterAuctionEvent.SubmitResult(response.body.id)
                }

                is ApiResponse.Failure -> {
                    if (response.responseCode == 400) {
                        response.error?.let {
                            val jsonObject = JSONObject(it)
                            val message = jsonObject.getString("message")
                            _event.value = RegisterAuctionEvent.SubmitError(message)
                        }
                    }
                }

                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
            isLoading = false
        }
    }

    private fun judgeValidInputs(): Boolean {
        val isNoImage = images.value?.isEmpty() ?: true
        val title = title.value
        val category = _category.value?.id
        val description = description.value
        val startPrice = startPrice.value
        val bidUnit = bidUnit.value
        val closingTime = closingTime.value
        val directRegion = _directRegion.value?.size ?: 0

        if (isNoImage ||
            title.isNullOrBlank() ||
            category == null ||
            description.isNullOrBlank() ||
            startPrice.isNullOrBlank() ||
            bidUnit.isNullOrBlank() ||
            closingTime == null ||
            directRegion == 0
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

    private fun createRequestModel(): RegisterAuctionRequest {
        val title = title.value ?: ""
        val category = _category.value?.id ?: -1
        val description = description.value ?: ""
        val startPrice = startPrice.value?.toInt() ?: 0
        val bidUnit = bidUnit.value?.toInt() ?: 0
        val closingTime = closingTime.value.toString() + ":00" // seconds
        val regions = _directRegion.value?.map { it.id } ?: emptyList()

        return RegisterAuctionRequest(
            title,
            category,
            description,
            startPrice,
            bidUnit,
            closingTime,
            regions,
        )
    }

    fun addImages(images: List<RegisterImageModel>) {
        _images.value = _images.value?.plus(images) ?: images
    }

    fun deleteImage(image: RegisterImageModel) {
        _images.value = _images.value?.minus(image) ?: emptyList()
    }

    fun pickImages() {
        _event.value = RegisterAuctionEvent.MultipleMediaPicker
    }

    fun setClosingTimeEvent() {
        _event.value =
            RegisterAuctionEvent.ClosingTimePicker(_closingTime.value ?: LocalDateTime.now())
    }

    fun setExitEvent() {
        _event.value = RegisterAuctionEvent.Exit
    }

    fun setPickCategoryEvent() {
        _event.value = RegisterAuctionEvent.PickCategory
    }

    fun setPickRegionEvent() {
        _event.value = RegisterAuctionEvent.PickRegion
    }

    private fun setBlankExistEvent() {
        _event.value = RegisterAuctionEvent.InputErrorEvent.BlankExistEvent
    }

    private fun setInvalidValueInputEvent() {
        _event.value = RegisterAuctionEvent.InputErrorEvent.InvalidValueInputEvent
    }

    fun setDeleteImageEvent(image: RegisterImageModel) {
        _event.value = RegisterAuctionEvent.DeleteImage(image)
    }

    fun setCategory(category: CategoryModel) {
        _category.value = category
    }

    fun setRegion(regions: List<RegionSelectionModel>) {
        _directRegion.value = regions
    }

    sealed class RegisterAuctionEvent {
        object Exit : RegisterAuctionEvent()
        data class SubmitError(val message: String) : RegisterAuctionEvent()
        class ClosingTimePicker(val dateTime: LocalDateTime) : RegisterAuctionEvent()
        class SubmitResult(val id: Long) : RegisterAuctionEvent()
        sealed class InputErrorEvent : RegisterAuctionEvent() {
            object InvalidValueInputEvent : InputErrorEvent()
            object BlankExistEvent : InputErrorEvent()
        }

        class DeleteImage(val image: RegisterImageModel) : RegisterAuctionEvent()
        object MultipleMediaPicker : RegisterAuctionEvent()

        object PickCategory : RegisterAuctionEvent()
        object PickRegion : RegisterAuctionEvent()
    }

    companion object {
        const val MAXIMUM_IMAGE_SIZE = 10
    }
}
