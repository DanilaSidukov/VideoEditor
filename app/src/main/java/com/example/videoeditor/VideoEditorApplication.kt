package com.example.videoeditor

import android.app.Application
import android.content.Context
import com.example.videoeditor.di.Injector

class VideoEditorApplication: Application() {

    companion object {

        private var injector: Injector? = null

        private fun init (context: Context) {
            injector = Injector(context)
        }

        fun getInjector() = injector!!

    }

    override fun onCreate() {
        super.onCreate()
        init(this)
    }

}