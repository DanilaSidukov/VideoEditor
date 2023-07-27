package com.example.videoeditor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.videoeditor.VideoEditorApplication
import com.example.videoeditor.ui.screens.choosemedia.ChooseMediaViewModel
import com.example.videoeditor.utility.EditViewModel

inline fun <reified VM: ViewModel> constructViewModel(): ViewModelProvider.NewInstanceFactory =
    object :ViewModelProvider.NewInstanceFactory() {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return when(VM::class) {
                ChooseMediaViewModel::class -> ChooseMediaViewModel(
                        VideoEditorApplication.getInjector().mediaDataProvider
                )
                EditViewModel::class -> EditViewModel(
                    VideoEditorApplication.getInjector().videoManager
                )
                else -> throw ClassNotFoundException()
            } as T
        }
    }

@Composable
inline fun <reified VM : ViewModel> viewModelWithFactory() =
    androidx.lifecycle.viewmodel.compose.viewModel<VM>(
        factory = constructViewModel<VM>()
    )