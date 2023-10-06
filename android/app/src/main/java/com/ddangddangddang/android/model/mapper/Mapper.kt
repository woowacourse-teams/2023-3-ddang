package com.ddangddangddang.android.model.mapper

interface Mapper<T, R> {
    fun R.toPresentation(): T
}
