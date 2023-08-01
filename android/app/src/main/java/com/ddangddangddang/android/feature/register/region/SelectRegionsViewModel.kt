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

    fun setExitEvent() {
        _event.value = SelectRegionsEvent.Exit
    }

    fun setFirstRegionSelection(id: Long) {
        _firstRegions.value?.let { regionSelectionModels ->
            _firstRegions.value = regionSelectionModels.map { regionSelectionModel ->
                if (regionSelectionModel.id == id) {
                    regionSelectionModel.copy(isChecked = true)
                } else {
                    regionSelectionModel.copy(isChecked = false)
                }
            }
        }
        _secondRegions.value = regionRepository.getSecondRegions(id).map { it.toPresentation() }
    }

    sealed class SelectRegionsEvent {
        object Exit : SelectRegionsEvent()
        class FirstRegionSelectionChanged(val secondRegions: List<RegionSelectionModel>) :
            SelectRegionsEvent()

        class SecondRegionSelectionChanged(val thirdRegions: List<RegionSelectionModel>) :
            SelectRegionsEvent()

        class ThirdRegionSelectionChanged() : SelectRegionsEvent()
    }
}
