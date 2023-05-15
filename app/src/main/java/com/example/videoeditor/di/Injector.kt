package com.example.videoeditor.di

import android.content.Context
import com.example.videoeditor.data.service.MediaDataProvider

class Injector(context: Context) {

    val mediaDataProvider = MediaDataProvider(context)

}