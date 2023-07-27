package com.example.videoeditor.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.videoeditor.data.logic.convertIntToDuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class MediaDataProvider(private val context: Context) {

    private lateinit var collection: Uri

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("Recycle")
    fun getAllMediaData(): Flow<MediaFiles> = channelFlow {
        launch {
            val videoProjection = arrayOf(
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DATE_ADDED,
            )
            collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Video.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
                else MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val cursorVideo = context.contentResolver.query(
                collection,
                videoProjection,
                null, null, "${MediaStore.Video.VideoColumns.DATE_ADDED} DESC"
            )
            try {
                cursorVideo?.moveToFirst()
                if (cursorVideo != null) {
                    println("position = ${cursorVideo.position}")
                    do {
                        channel.send(
                            MediaFiles(
                                mediaId = cursorVideo.getString(0),
                                mediaPath = cursorVideo.getString(1),
                                mediaDuration = convertIntToDuration(
                                    cursorVideo.getString(2).toInt()
                                ),
                                mediaDateAdded = cursorVideo.getString(3).toInt(),
                                isVideo = true
                            )
                        )
                        cursorVideo.moveToNext()
                    } while (cursorVideo.position != cursorVideo.count)
                }

            } catch (e: Exception) {
                Log.e("CURSOR VIDEO ERROR", e.message.toString())
            } finally {
                cursorVideo?.close()
            }
        }
        launch {
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
                    do {
                        channel.send(
                            MediaFiles(
                                mediaId = cursorImage.getString(0),
                                mediaName = cursorImage.getString(1),
                                mediaDateAdded = cursorImage.getInt(2),
                                mediaPath = cursorImage.getString(3),
                                isVideo = false
                            )
                        )
                    } while (cursorImage.moveToNext())
                }
            } catch (e: Exception) {
                Log.e("CURSOR ERROR", e.message.toString())
            } finally {
                cursorImage!!.close()
            }
        }
    }
}

data class MediaFiles(
    val mediaId: String,
    val mediaName: String? = null,
    val mediaPath: String? = null,
    val mediaDuration: String? = null,
    val mediaDateAdded: Int? = null,
    val mediaBitmap: Bitmap? = null,
    val isVideo: Boolean,
)

