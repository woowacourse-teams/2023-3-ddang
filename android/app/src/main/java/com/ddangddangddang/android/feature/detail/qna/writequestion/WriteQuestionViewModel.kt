package com.ddangddangddang.android.feature.detail.qna.writequestion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class WriteQuestionViewModel(private val repository: AuctionRepository) : ViewModel() {
    private val _event: SingleLiveEvent<WriteQuestionEvent> = SingleLiveEvent()
    val event: LiveData<WriteQuestionEvent>
        get() = _event

    val content = MutableLiveData("")

    fun cancel() {
        _event.value = WriteQuestionEvent.Cancel
    }

    fun submit() {
        _event.value = WriteQuestionEvent.SubmitQuestion
    }

    sealed class WriteQuestionEvent {
        object Cancel : WriteQuestionEvent()
        object SubmitQuestion : WriteQuestionEvent()
    }
}
