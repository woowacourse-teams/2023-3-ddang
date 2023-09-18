package com.ddangddangddang.android.util.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun Uri.toAdjustImageFile(context: Context): File? {
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
