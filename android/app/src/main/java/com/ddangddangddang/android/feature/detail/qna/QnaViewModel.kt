package com.ddangddangddang.android.feature.detail.qna

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddangddangddang.android.model.QnAModel
import com.ddangddangddang.android.model.QnAStatusModel

class QnaViewModel : ViewModel() {

    private val _qnas = MutableLiveData(
        listOf(
            QnAModel.QuestionAndAnswer(
                0,
                "판매자고 질문자 아님",
                "asdf",
                "asdf",
                QnAStatusModel.WAITING,
                true,
                false,
                false,
            ),
            QnAModel.QuestionAndAnswer(
                1,
                "판매자 아니고 질문자 아님",
                "asdf",
                "asdf",
                QnAStatusModel.COMPLETE,
                false,
                true,
                false,
            ),
            QnAModel.QuestionAndAnswer(
                2,
                "둘다 아님",
                "asdf",
                "asdf",
                QnAStatusModel.WAITING,
                false,
                false,
                false,
            ),
        ),
    )

    val qnas: LiveData<List<QnAModel.QuestionAndAnswer>>
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
