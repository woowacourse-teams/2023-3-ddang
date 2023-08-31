package com.ddangddangddang.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(val name: String, val id: Long, val isChecked: Boolean = false) : Parcelable
