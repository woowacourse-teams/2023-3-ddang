package com.ddangddangddang.android.feature.userInfoChange

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddangddangddang.android.model.ProfileModel
import com.ddangddangddang.android.util.livedata.SingleLiveEvent
import com.ddangddangddang.data.model.request.ProfileUpdateRequest
import com.ddangddangddang.data.remote.ApiResponse
import com.ddangddangddang.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UserInfoChangeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private val _profile: MutableLiveData<Uri> = MutableLiveData()
    val profile: LiveData<Uri>
        get() = _profile

    val userNickname: MutableLiveData<String> = MutableLiveData()

    fun setInitUserInfo(profileModel: ProfileModel, defaultUri: Uri) {
        _profile.value = profileModel.profileImage?.let { Uri.parse(it) } ?: defaultUri
        userNickname.value = profileModel.name
    }

    fun setExitEvent() {
        _event.value = Event.Exit
    }

    fun selectProfileImage() {
        _event.value = Event.NavigateToSelectProfileImage
    }

    fun setImage(uri: Uri) {
        _profile.value = uri
    }

    fun submitProfile(context: Context) {
        val name = userNickname.value ?: return
        val profileImageUri = profile.value ?: return
        viewModelScope.launch {
            val file = profileImageUri.toAdjustImageFile(context) ?: return@launch
            when (userRepository.updateProfile(file, ProfileUpdateRequest(name))) {
                is ApiResponse.Success -> {}
                is ApiResponse.Failure -> {}
                is ApiResponse.NetworkError -> {}
                is ApiResponse.Unexpected -> {}
            }
        }
    }

    private fun Uri.toAdjustImageFile(context: Context): File? {
        val bitmap = toBitmap(context) ?: return null

        val file = createAdjustImageFile(bitmap, context.cacheDir)

        val orientation = context.contentResolver
            .openInputStream(this)?.use {
            ExifInterface(it)
        }?.getAttribute(ExifInterface.TAG_ORIENTATION)
        orientation?.let { file.setOrientation(it) }

        return file
    }

    private fun Uri.toBitmap(context: Context): Bitmap? {
        return context.contentResolver
            .openInputStream(this)?.use {
            BitmapFactory.decodeStream(it)
        }
    }

    private fun createAdjustImageFile(bitmap: Bitmap, directory: File): File {
        val byteArrayOutputStream = ByteArrayOutputStream()
        Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
            .compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

        val tempFile = File.createTempFile("resized_image", ".jpg", directory)
        FileOutputStream(tempFile).use {
            it.write(byteArrayOutputStream.toByteArray())
        }
        return tempFile
    }

    private fun File.setOrientation(orientation: String) {
        ExifInterface(this.path).apply {
            setAttribute(ExifInterface.TAG_ORIENTATION, orientation)
            saveAttributes()
        }
    }

    sealed class Event {
        object Exit : Event()
        object NavigateToSelectProfileImage : Event()
    }
}
