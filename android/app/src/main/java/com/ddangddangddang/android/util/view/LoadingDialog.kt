package com.ddangddangddang.android.util.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RawRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.ddangddangddang.android.R
import com.ddangddangddang.android.databinding.ViewLoadingDialogBinding

class LoadingDialog(context: Context, @RawRes rawResId: Int) : Dialog(context) {
    private val binding: ViewLoadingDialogBinding = ViewLoadingDialogBinding.inflate(layoutInflater)

    init {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setCancelable(false)
        binding.lavLoading.setAnimation(rawResId)
    }
}

fun Context.observeLoadingWithDialog(
    lifecycleOwner: LifecycleOwner,
    loadingLiveData: LiveData<Boolean>,
    containerViewGroup: ViewGroup? = null,
    @RawRes loadingAnimationResId: Int = R.raw.default_loading,
    onLoadingStarted: () -> Unit = {},
    onLoadingFinished: () -> Unit = {},
) {
    val loadingDialog = LoadingDialog(this, loadingAnimationResId)
    loadingLiveData.observe(lifecycleOwner) { isLoading ->
        if (isLoading) {
            loadingDialog.show()
            containerViewGroup?.visibility = View.GONE
            onLoadingStarted()
        } else {
            containerViewGroup?.visibility = View.VISIBLE
            loadingDialog.dismiss()
            onLoadingFinished()
        }
    }
}
