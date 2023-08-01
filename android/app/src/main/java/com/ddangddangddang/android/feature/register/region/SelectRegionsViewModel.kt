package com.ddangddangddang.android.feature.register.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.feature.common.regionRepository
import com.ddangddangddang.android.model.RegionSelectionModel
import com.ddangddangddang.android.model.mapper.RegionModelMapper.toPresentation
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.repository.RegionRepository

class SelectRegionsViewModel(regionRepository: RegionRepository) : ViewModel() {
    private val _event: SingleLiveEvent<SelectRegionsEvent> =
        SingleLiveEvent()
    val event: LiveData<SelectRegionsEvent>
        get() = _event

    private val _firstRegions = MutableLiveData(
        regionRepository.getFirstRegions().map { it.toPresentation() },
    )
    val firstRegions: LiveData<List<RegionSelectionModel>>
        get() = _firstRegions

    private val _secondRegions = MutableLiveData<List<RegionSelectionModel>>()
    val secondRegions: LiveData<List<RegionSelectionModel>>
        get() = _secondRegions

    private val _thirdRegions = MutableLiveData<List<RegionSelectionModel>>()
    val thirdRegions: LiveData<List<RegionSelectionModel>>
        get() = _thirdRegions

    private val regionSelections = mutableListOf<RegionSelectionModel>() // 임시

    fun setExitEvent() {
        _event.value = SelectRegionsEvent.Exit
    }

    fun setFirstRegionSelection(id: Long) {
        _firstRegions.value?.let { regionSelectionModels ->
            _firstRegions.value = regionSelectionModels.changeIsChecked(id)
        }
        _secondRegions.value = regionRepository.getSecondRegions(id).map { it.toPresentation() }
    }

    fun setSecondRegionSelection(id: Long) {
        _secondRegions.value?.let { regionSelectionModels ->
            _secondRegions.value = regionSelectionModels.changeIsChecked(id)
        }
        _thirdRegions.value = regionRepository.getThirdRegions(id).map { it.toPresentation() }
    }

    fun addRegion(thirdId: Long) {
        // 이미 있는 칩이면 종료
        if (regionSelections.any { it.id == (thirdId) }) return

        // 없는 칩이면 추가
        val first = _firstRegions.value?.find { it.isChecked }
        val second = _secondRegions.value?.find { it.isChecked }
        val third = _thirdRegions.value?.find { it.id == thirdId }

        if (first != null && second != null && third != null) {
            val newItem = third.copy(name = "${first.name} ${second.name} ${third.name}")
            regionSelections.add(newItem)
            _event.value = SelectRegionsEvent.AddRegion(newItem)
        }
    }

    fun deleteRegion(thirdId: Long) {
        regionSelections.removeIf { it.id == thirdId }
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
        data class AddRegion(val newRegion: RegionSelectionModel) : SelectRegionsEvent()
    }
}
