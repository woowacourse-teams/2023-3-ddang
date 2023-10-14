package com.ddangddangddang.android.model.mapper

import com.ddangddangddang.android.model.QnaModel
import com.ddangddangddang.android.model.QnaStatusModel
import com.ddangddangddang.data.model.response.QnaResponse

object QnaModelMapper {
    fun QnaResponse.toPresentation(isOwner: Boolean): QnaModel {
        return QnaModel(
            qnas.map {
                QnaModel.QuestionAndAnswerModel(
                    it.question.toPresentation(),
                    it.answer?.toPresentation(),
                    isOwner,
                    false,
                    QnaStatusModel.find(it.answer != null),
                )
            },
        )
    }

    private fun QnaResponse.Question.toPresentation(): QnaModel.QuestionModel {
        return QnaModel.QuestionModel(id, createdTime, content, isQuestioner)
    }

    private fun QnaResponse.Answer.toPresentation(): QnaModel.AnswerModel {
        return QnaModel.AnswerModel(id, createdTime, content)
    }
}
