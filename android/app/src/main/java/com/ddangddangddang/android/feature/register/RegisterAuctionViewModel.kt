package com.ddangddangddang.android.feature.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.feature.common.ErrorType
import com.ddangddangddang.android.model.CategoryModel
import com.ddangddangddang.android.model.RegionSelectionModel
import com.ddangddangddang.android.model.RegisterImageModel
import com.ddangddangddang.android.util.image.toAdjustImageFile
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.RegisterAuctionRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RegisterAuctionViewModel @Inject constructor(private val repository: AuctionRepository) :
    ViewModel() {
    // EditText Values - Two Way Binding
    val title: MutableLiveData<String> = MutableLiveData("")
    val description: MutableLiveData<String> = MutableLiveData("")
    private val _startPrice: MutableLiveData<BigInteger> = MutableLiveData()
    val startPrice: LiveData<BigInteger>
        get() = _startPrice
    private val _bidUnit: MutableLiveData<BigInteger> = MutableLiveData()
    val bidUnit: LiveData<BigInteger>
        get() = _bidUnit

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
                    _event.value =
                        RegisterAuctionEvent.SubmitError(ErrorType.FAILURE(response.error))
                }

                is ApiResponse.NetworkError -> {
                    _event.value = RegisterAuctionEvent.SubmitError(ErrorType.NETWORK_ERROR)
                }

                is ApiResponse.Unexpected -> {
                    _event.value = RegisterAuctionEvent.SubmitError(ErrorType.UNEXPECTED)
                }
            }
            isLoading = false
        }
    }

    private fun judgeValidInputs(): Boolean {
        val isNoImage = images.value?.isEmpty() ?: true
        val title = title.value
        val category = _category.value?.id
        val description = description.value
        val startPrice = _startPrice.value
        val bidUnit = bidUnit.value
        val closingTime = closingTime.value
        val directRegion = _directRegion.value?.size ?: 0

        if (isNoImage ||
            title.isNullOrBlank() ||
            category == null ||
            description.isNullOrBlank() ||
            startPrice == null ||
            bidUnit == null ||
            closingTime == null ||
            directRegion == 0
        ) {
            setBlankExistEvent()
            return false
        }
        return true
    }

    private fun createRequestModel(): RegisterAuctionRequest {
        val title = title.value ?: ""
        val category = _category.value?.id ?: -1
        val description = description.value ?: ""
        val startPrice = _startPrice.value?.toInt() ?: 0
        val bidUnit = bidUnit.value?.toInt() ?: 0
        val closingTime =
            closingTime.value?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm':00'")) ?: ""
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

    fun setStartPrice(text: String) {
        val convertedPrice = convertStringPriceToInt(text)
        _startPrice.value = convertedPrice
    }

    fun setBidUnit(text: String) {
        val convertedPrice = convertStringPriceToInt(text)
        _bidUnit.value = convertedPrice
    }

    private fun convertStringPriceToInt(text: String): BigInteger {
        val originalValue = text.replace(",", "") // 문자열 내 들어있는 콤마를 모두 제거
        val priceValue = originalValue.substringBefore(SUFFIX_INPUT_PRICE.trim()).trim() // " 원"
        val parsedValue =
            priceValue.toBigIntegerOrNull() ?: return ZERO.toBigInteger() // 입력에 문자가 섞인 경우
        if (parsedValue > MAX_PRICE.toBigInteger()) return MAX_PRICE.toBigInteger()
        return parsedValue
    }

    fun setExitEvent() {
        _event.value = RegisterAuctionEvent.Exit
    }

    fun setPickCategoryEvent() {
        _event.value = RegisterAuctionEvent.PickCategory
    }

    fun setPickRegionEvent() {
        _event.value = RegisterAuctionEvent.PickRegion(_directRegion.value ?: emptyList())
    }

    private fun setBlankExistEvent() {
        _event.value = RegisterAuctionEvent.InputErrorEvent.BlankExistEvent
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
        data class SubmitError(val errorType: ErrorType) : RegisterAuctionEvent()
        class ClosingTimePicker(val dateTime: LocalDateTime) : RegisterAuctionEvent()
        class SubmitResult(val id: Long) : RegisterAuctionEvent()
        sealed class InputErrorEvent : RegisterAuctionEvent() {
            object InvalidValueInputEvent : InputErrorEvent()
            object BlankExistEvent : InputErrorEvent()
        }

        class DeleteImage(val image: RegisterImageModel) : RegisterAuctionEvent()
        object MultipleMediaPicker : RegisterAuctionEvent()

        object PickCategory : RegisterAuctionEvent()
        class PickRegion(val regionSelected: List<RegionSelectionModel>) : RegisterAuctionEvent()
    }

    companion object {
        const val MAXIMUM_IMAGE_SIZE = 10
        const val SUFFIX_INPUT_PRICE = " 원"
        private const val ZERO = 0
        private const val MAX_PRICE = 2100000000
    }
}
