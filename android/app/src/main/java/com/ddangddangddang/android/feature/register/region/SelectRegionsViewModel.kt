package com.ddangddangddang.android.feature.register.region

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.RegionSelectionModel
import com.ddangddangddang.android.model.mapper.RegionModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.RegionRepository
import kotlinx.coroutines.launch

private typealias FirstId = Long
private typealias SecondId = Long

class SelectRegionsViewModel(private val regionRepository: RegionRepository) : ViewModel() {
    private val _event: SingleLiveEvent<SelectRegionsEvent> =
        SingleLiveEvent()
    val event: LiveData<SelectRegionsEvent>
        get() = _event

    private val _firstRegions = MutableLiveData<List<RegionSelectionModel>>()
    val firstRegions: LiveData<List<RegionSelectionModel>>
        get() = _firstRegions

    private val _secondRegions = MutableLiveData<List<RegionSelectionModel>>()
    val secondRegions: LiveData<List<RegionSelectionModel>>
        get() = _secondRegions

    private val _thirdRegions = MutableLiveData<List<RegionSelectionModel>>()
    val thirdRegions: LiveData<List<RegionSelectionModel>>
        get() = _thirdRegions

    private val _regionSelections = MutableLiveData<List<RegionSelectionModel>>(emptyList())
    val regionSelections: LiveData<List<RegionSelectionModel>>
        get() = _regionSelections

    private val secondRegionsCache: MutableMap<FirstId, List<RegionSelectionModel>> = mutableMapOf()
    private val thirdRegionsCache: MutableMap<SecondId, List<RegionSelectionModel>> = mutableMapOf()

    fun loadFirstRegions() {
        viewModelScope.launch {
            when (val response = regionRepository.getFirstRegions()) {
                is ApiResponse.Success -> {
                    val regions = response.body.map { it.toPresentation() }
                    _firstRegions.value = regions
                }
                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun setExitEvent() {
        _event.value = SelectRegionsEvent.Exit
    }

    fun setFirstRegionSelection(id: Long) {
        _firstRegions.value?.let { regionSelectionModels ->
            _firstRegions.value = regionSelectionModels.changeIsChecked(id)
        }
        changeSecondRegions(id)
    }

    private fun changeSecondRegions(firstId: Long) {
        // 캐싱되어있는 경우
        if (!secondRegionsCache[firstId].isNullOrEmpty()) {
            _secondRegions.value = secondRegionsCache[firstId]
            _thirdRegions.value = emptyList()
            return
        }

        viewModelScope.launch {
            when (val response = regionRepository.getSecondRegions(firstId)) {
                is ApiResponse.Success -> {
                    val regions = response.body.map { it.toPresentation() }
                    _secondRegions.value = regions
                    _thirdRegions.value = emptyList()
                    secondRegionsCache[firstId] = regions
                }

                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun setSecondRegionSelection(secondId: Long) {
        val firstId = _firstRegions.value?.find { it.isChecked }?.id ?: return

        _secondRegions.value?.let { regionSelectionModels ->
            _secondRegions.value = regionSelectionModels.changeIsChecked(secondId)
        }

        changeThirdRegions(firstId, secondId)
    }

    private fun changeThirdRegions(firstId: Long, secondId: Long) {
        // 캐싱 되어있는 경우
        if (!thirdRegionsCache[secondId].isNullOrEmpty()) {
            _thirdRegions.value = thirdRegionsCache[secondId]
            return
        }

        viewModelScope.launch {
            when (val response = regionRepository.getThirdRegions(firstId, secondId)) {
                is ApiResponse.Success -> {
                    val regions = response.body.map { it.toPresentation() }
                    _thirdRegions.value = regions
                    thirdRegionsCache[secondId] = regions
                }

                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    fun addRegion(thirdId: Long) {
        _regionSelections.value?.let { regions ->
            // 이미 있는 칩이면 종료
            if (regions.any { it.id == (thirdId) }) return

            // 없는 칩이면 추가
            val first = _firstRegions.value?.find { it.isChecked }
            val second = _secondRegions.value?.find { it.isChecked }
            val third = _thirdRegions.value?.find { it.id == thirdId }

            if (first != null && second != null && third != null) {
                val newItem = third.copy(name = "${first.name} ${second.name} ${third.name}")
                _regionSelections.value = regions + newItem
            }
        }
    }

    fun deleteRegion(thirdId: Long) {
        _regionSelections.value?.let { regions ->
            regions.find { it.id == thirdId }?.let {
                _regionSelections.value = regions - it
            }
        }
    }

    fun submit() {
        Log.d("test", "submit")
        _event.value = SelectRegionsEvent.Submit(_regionSelections.value ?: emptyList())
    }

    // checked를 바꿔주는 확장 함수
    private fun List<RegionSelectionModel>.changeIsChecked(checkedId: Long): List<RegionSelectionModel> =
        this.map {
            if (it.id == checkedId) {
                it.copy(isChecked = true)
            } else {
                it.copy(isChecked = false)
            }
        }

    sealed class SelectRegionsEvent {
        object Exit : SelectRegionsEvent()
        data class Submit(val regions: List<RegionSelectionModel>) : SelectRegionsEvent()
    }
}
