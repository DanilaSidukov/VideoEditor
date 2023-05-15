package com.example.videoeditor

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.example.videoeditor.data.service.MediaDataProvider
import com.example.videoeditor.screens.MainScreen
import com.example.videoeditor.screens.choosemedia.ChooseMediaScreen
import com.example.videoeditor.screens.choosemedia.ChooseMediaViewModel


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemUI()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val vm = ChooseMediaViewModel(
            MediaDataProvider(
                this
            )
        )

        setContent {

            val navController = rememberNavController()

            //MainScreen(navController = navController)
            ChooseMediaScreen(chooseMediaViewModel = vm,
                onItemClicked = {})
        }

    }

    private fun hideSystemUI(){
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}