package com.example.videoeditor.data.logic

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.util.concurrent.TimeUnit

fun convertImageToBitmap(context: Context, imageUri: String): Bitmap{
    val uri = Uri.fromFile(File(imageUri))
    return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
}

fun convertFirstFrameToBitmap(context: Context, path: String): Bitmap{
    return MediaStore.Video.Thumbnails.getThumbnail(
        context.contentResolver,
        ContentUris.parseId(Uri.fromFile(File(path))),
        MediaStore.Video.Thumbnails.MINI_KIND,
        (BitmapFactory.Options()),
    )
}

fun convertIntToDuration(milliseconds: Int): String{
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds.toLong())
    val minutes = if (hours.toInt() == 0) TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())
    else milliseconds - TimeUnit.HOURS.toMinutes(hours)
    val seconds = if (minutes.toInt() == 0) TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong())
    else milliseconds - TimeUnit.MINUTES.toSeconds(milliseconds.toLong())
    return if (hours.toInt() == 0) String.format("%02d: %02d", minutes, seconds)
    else String.format("%02d: %02d: %02d", hours, minutes, seconds)
}