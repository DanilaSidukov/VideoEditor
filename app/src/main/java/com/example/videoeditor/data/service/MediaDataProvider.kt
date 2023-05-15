package com.example.videoeditor.data.service

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.MergeCursor
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File

class MediaDataProvider( private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("Recycle")
    fun getAllMediaData():List<MediaFiles>{

        val allMedia = mutableListOf<MediaFiles>()

//        val videoProjection = arrayOf(
//            MediaStore.Video.VideoColumns._ID,
//            MediaStore.Video.VideoColumns.DISPLAY_NAME,
//            MediaStore.Video.VideoColumns.DURATION,
//            MediaStore.Video.VideoColumns.DATE_ADDED,
//            MediaStore.Video.VideoColumns.DATA
//        )
//
//        val cursorVideo = context.contentResolver.query(
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//            videoProjection,
//            null, null, null
//        )
//
//        try {
//            cursorVideo.moveToFirst()
//            while (cursorVideo.moveToNext()){
//                allMedia.add(
//                    MediaFiles(
//                        mediaId = cursorVideo.getString(0),
//                        mediaName = cursorVideo.getString(1),
//                        mediaDuration = cursorVideo.getInt(2),
//                        mediaDateAdded = cursorVideo.getInt(4),
//                        mediaPath = cursorVideo.getString(5),
//                        mediaBitmap = convertFirstFrameToBitmap(cursorVideo.getString(5))
//                    )
//                )
//            }
//        } catch (e: Exception){
//            Log.e("CURSOR ERROR", e.message.toString())
//        } finally {
//            cursorVideo.close()
//        }

        val imageProjection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATA
        )

        val cursorImage = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null, null, null
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
            println("list = $allMedia")
        } catch (e: Exception){
            Log.e("CURSOR ERROR", e.message.toString())
        } finally {
            cursorImage!!.close()
        }

        return allMedia

    }

}

data class MediaFiles(
    val mediaId: String? = null,
    val mediaName: String? = null,
    val mediaPath: String? = null,
    val mediaDuration: Int? = null,
    val mediaDateAdded: Int? = null,
    val mediaBitmap: Bitmap? = null,
)

fun convertImageToBitmap(context: Context, imageUri: String): Bitmap{
    val uri = Uri.fromFile(File(imageUri))
    return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
}

fun convertFirstFrameToBitmap(path: String): Bitmap{
    return ThumbnailUtils.createVideoThumbnail(Uri.fromFile(File(path)).toString(), MediaStore.Images.Thumbnails.MINI_KIND)!!
}