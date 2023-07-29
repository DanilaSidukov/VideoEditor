package com.example.videoeditor.data.logic

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.provider.MediaStore.Video.Thumbnails.MINI_KIND
import android.util.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import java.io.File
import java.util.concurrent.TimeUnit

fun convertImageToBitmap(context: Context, imageUri: String): ImageBitmap {
    val uri = Uri.fromFile(File(imageUri))
    return ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri)).asImageBitmap()
}

fun convertFirstFrameToBitmap(context: Context, path: String): ImageBitmap{
    return context.contentResolver.loadThumbnail(path.toUri(), Size(150, 150), CancellationSignal()).asImageBitmap()
}

fun convertIntToDuration(milliseconds: Int?): String?{
    if (milliseconds == null) return null
    else {
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds.toLong())
        val minutes = if (hours.toInt() == 0) TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())
            else milliseconds - TimeUnit.HOURS.toMinutes(hours)
        val seconds = if (minutes.toInt() == 0) TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong())
            else milliseconds - TimeUnit.MINUTES.toSeconds(milliseconds.toLong())
        return if (hours.toInt() == 0) String.format("%02d: %02d", minutes, seconds)
            else String.format("%02d: %02d: %02d", hours, minutes, seconds)
    }
}