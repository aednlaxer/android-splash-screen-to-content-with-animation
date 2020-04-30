package com.example.splashscreendemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable transparent status and navigation bar
        goEdgeToEdge()

        // Animate splash screen logo
        findViewById<SplashView>(R.id.splash_view)
            .animateLogo()
    }

    private fun goEdgeToEdge() {
        val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        this.window?.apply {
            decorView.systemUiVisibility = flags
            statusBarColor = Color.TRANSPARENT
        }
    }
}
