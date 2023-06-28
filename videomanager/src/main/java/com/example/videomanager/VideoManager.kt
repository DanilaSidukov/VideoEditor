package com.example.videomanager

class VideoManager {

    init {
        System.loadLibrary("video_manager")
    }

    fun load(uri: String) {
        nativeLoad(uri)
    }

    private external fun nativeLoad(uri: String)

}