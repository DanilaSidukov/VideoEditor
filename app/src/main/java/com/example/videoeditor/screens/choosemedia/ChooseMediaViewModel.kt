package com.example.videoeditor.screens.choosemedia

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoeditor.data.service.MediaDataProvider
import com.example.videoeditor.data.service.MediaFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChooseMediaViewModel(
    private val mediaDataProvider: MediaDataProvider,
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun getMediaList(): List<MediaFiles> {

        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            mediaDataProvider.getAllMediaData()
        }
    }
}