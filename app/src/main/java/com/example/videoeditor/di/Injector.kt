package com.example.videoeditor.di

import android.content.Context
import com.example.videoeditor.data.service.MediaDataProvider
import com.example.videomanager.VideoManager

class Injector(context: Context) {

    val mediaDataProvider = MediaDataProvider(context)

    val videoManager = VideoManager()

}