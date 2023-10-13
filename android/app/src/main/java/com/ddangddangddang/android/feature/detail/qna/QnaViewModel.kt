package com.ddangddangddang.android.feature.detail.qna

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.QnaModel
import com.ddangddangddang.android.model.QnaStatusModel

class QnaViewModel : ViewModel() {

    private val _qnas = MutableLiveData(
        listOf(
            QnaModel.QuestionAndAnswer(
                0,
                "판매자고 질문자 아님",
                "asdf",
                "asdf",
                QnaStatusModel.WAITING,
                true,
                false,
                false,
            ),
            QnaModel.QuestionAndAnswer(
                1,
                "판매자 아니고 질문자 아님",
                "asdf",
                "asdf",
                QnaStatusModel.COMPLETE,
                false,
                true,
                false,
            ),
            QnaModel.QuestionAndAnswer(
                2,
                "둘다 아님",
                "asdf",
                "asdf",
                QnaStatusModel.WAITING,
                false,
                false,
                false,
            ),
        ),
    )

    val qnas: LiveData<List<QnaModel.QuestionAndAnswer>>
        get() = _qnas

    fun selectQna(id: Long) {
        _qnas.value?.let {
            _qnas.value = it.map { model ->
                if (model.isPicked && model.id == id) {
                    model.copy(isPicked = false)
                } else {
                    model.copy(isPicked = model.id == id)
                }
            }
        }
    }
}
