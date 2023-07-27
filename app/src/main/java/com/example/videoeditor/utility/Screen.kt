package com.example.videoeditor.utility

sealed class Screen(val route: String){

    object ChooseMedia: Screen("choose_media")

    object GetStoragePermission: Screen("get_storage_permission")

    object Edit: Screen("edit")

}
