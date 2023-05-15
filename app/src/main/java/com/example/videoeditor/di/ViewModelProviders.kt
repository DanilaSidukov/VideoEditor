package com.example.videoeditor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.videoeditor.VideoEditorApplication
import com.example.videoeditor.screens.choosemedia.ChooseMediaViewModel

inline fun <reified VM: ViewModel> constructViewModel(): ViewModelProvider.NewInstanceFactory =
    object :ViewModelProvider.NewInstanceFactory() {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return when(VM::class) {
                ChooseMediaViewModel::class -> ChooseMediaViewModel(
                        VideoEditorApplication.getInjector().mediaDataProvider
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