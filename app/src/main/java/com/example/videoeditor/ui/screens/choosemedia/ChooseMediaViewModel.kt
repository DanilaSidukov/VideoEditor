package com.example.videoeditor.ui.screens.choosemedia

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.videoeditor.data.service.MediaDataProvider
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.R)
class ChooseMediaViewModel(
    private val mediaDataProvider: MediaDataProvider,
) : ViewModel() {

    val mediaFlow = mediaDataProvider.getAllMediaData().onEach {
        println("received media: $it")
    }

}