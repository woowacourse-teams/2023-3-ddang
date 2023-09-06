package com.ddangddangddang.android.util.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.annotation.RawRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ViewLoadingDialogBinding

class LoadingDialog(context: Context) : Dialog(context) {
    private val binding: ViewLoadingDialogBinding = ViewLoadingDialogBinding.inflate(layoutInflater)

    init {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setCancelable(false)
    }

    fun startWithAnimation(@RawRes rawResId: Int) {
        binding.lavLoading.setAnimation(rawResId)
        this.show()
    }

    override fun onStart() {
        super.onStart()
        binding.lavLoading.playAnimation()
    }

    override fun dismiss() {
        super.dismiss()
        binding.lavLoading.pauseAnimation()
    }
}

fun Context.observeLoadingWithDialog(
    lifecycleOwner: LifecycleOwner,
    loadingLiveData: LiveData<Boolean>,
    @RawRes loadingAnimationResId: Int = R.raw.loading,
    onLoadingStarted: () -> Unit = {},
    onLoadingFinished: () -> Unit = {},
) {
    val loadingDialog = LoadingDialog(this)
    loadingLiveData.observe(lifecycleOwner) { isLoading ->
        if (isLoading) {
            loadingDialog.startWithAnimation(loadingAnimationResId)
            onLoadingStarted()
        } else {
            loadingDialog.dismiss()
            onLoadingFinished()
        }
    }
}
