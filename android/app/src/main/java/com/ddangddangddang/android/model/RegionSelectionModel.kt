package com.ddangddangddang.android.model

import java.io.Serializable

data class RegionSelectionModel(val id: Long, val name: String, val isChecked: Boolean = false) :
    Serializable
