package com.ddangddangddang.android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegionSelectionModel(val id: Long, val name: String, val isChecked: Boolean = false) :
    Parcelable
