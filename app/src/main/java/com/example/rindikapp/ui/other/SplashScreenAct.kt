package com.example.rindikapp.ui.other

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.rindikapp.R
import com.example.rindikapp.ui.main.MainActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splash = findViewById<ImageView>(R.id.iv_splash)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        splash.alpha = 0f
        splash.animate().setDuration(2500).alpha(1f).withEndAction{
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }


    }
}