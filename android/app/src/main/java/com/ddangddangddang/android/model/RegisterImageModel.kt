package com.ddangddangddang.android.model

import android.net.Uri

data class RegisterImageModel(val id: Long = System.currentTimeMillis(), val uri: Uri)
