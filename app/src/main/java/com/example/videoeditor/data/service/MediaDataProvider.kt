package com.example.videoeditor.data.service

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.MergeCursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.min

class MediaDataProvider( private val context: Context) {

    private lateinit var collection: Uri

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("Recycle")
    fun getAllMediaData():List<MediaFiles>{

        val allMedia = mutableListOf<MediaFiles>()

        val videoProjection = arrayOf(
            MediaStore.Video.VideoColumns._ID,
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Video.VideoColumns.DATE_ADDED,
        )

        collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val cursorVideo = context.contentResolver.query(
            collection,
            videoProjection,
            null, null, "${MediaStore.Video.VideoColumns.DATE_ADDED} DESC"
        )

        try {
            cursorVideo?.moveToFirst()
            if (cursorVideo != null){
                do {
                    allMedia.add(
                        MediaFiles(
                            mediaId = cursorVideo.getString(0) ,
                            mediaPath = cursorVideo.getString(1),
                            mediaDuration = convertIntToDuration(cursorVideo.getString(2).toInt()),
                            mediaDateAdded = cursorVideo.getString(3).toInt(),
                            mediaBitmap = convertFirstFrameToBitmap(context, cursorVideo.getString(0))
                        )
                    )
                }
                while (cursorVideo.moveToNext())
            }
        } catch (e: Exception){
            Log.e("CURSOR VIDEO ERROR", e.message.toString())
        } finally {
            cursorVideo?.close()
        }

        val imageProjection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATA
        )

        val cursorImage = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        try {
            cursorImage?.moveToFirst()
            if (cursorImage != null) {
                while (cursorImage.moveToNext()){
                    allMedia.add(
                        MediaFiles(
                            mediaId = cursorImage.getString(0),
                            mediaName = cursorImage.getString(1),
                            mediaDateAdded = cursorImage.getInt(2),
                            mediaPath = cursorImage.getString(3),
                            mediaBitmap = convertImageToBitmap(context, cursorImage.getString(3))
                        )
                    )
                }
            }
        } catch (e: Exception){
            Log.e("CURSOR ERROR", e.message.toString())
        } finally {
            cursorImage!!.close()
        }

        return allMedia.sortedByDescending { it.mediaDateAdded }

    }

}

data class MediaFiles(
    val mediaId: String? = null,
    val mediaName: String? = null,
    val mediaPath: String? = null,
    val mediaDuration: String? = null,
    val mediaDateAdded: Int? = null,
    val mediaBitmap: Bitmap? = null,
)

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