package com.example.splashscreendemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This starts next activity after several seconds. This code is for demo purposes only.
        // Never use splash screen like that
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            // Disable activity animations
            overridePendingTransition(0, 0)
            finish()
        }, 2000L)
    }
}
