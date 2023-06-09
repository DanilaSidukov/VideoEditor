package com.example.videoeditor.data

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Settings {

    companion object{
        val mockProjects = ProjectItem(
            previewImage = "res/drawable/mock_image.png",
            name = "LIGHTHOUSE",
            date = "11 DEC 2022"
        )
    }

}

// D:\AndroidStudioProjects\VideoEditor\app\src\main\res\drawable\mock_image.png

data class ProjectItem(
    var previewImage: String?,
    var name: String?,
    var date: String?
)