package com.example.videoeditor.data.logic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

fun ConverterImage(path: String): Bitmap {
    val filePath = "/${Environment.getExternalStorageDirectory().path}/$path"
    val imageFile: File = File(filePath)
    var fileInputStream: FileInputStream? = null
    try {
        fileInputStream = FileInputStream(imageFile)
    } catch (exception: FileNotFoundException) {
        exception.printStackTrace()
    }

    val bitmap: Bitmap = BitmapFactory.decodeStream(fileInputStream)
    val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    //Base64.encodeToString(byteArray, Base64.DEFAULT)
}