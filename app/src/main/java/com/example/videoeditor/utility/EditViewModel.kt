package com.example.videoeditor.utility

import androidx.lifecycle.ViewModel
import com.example.videomanager.VideoManager

class EditViewModel(
    private val videoManager: VideoManager
): ViewModel() {

    suspend fun loadVideoFile(filePath: String){
        videoManager.load(filePath)
    }

}