package com.example.rindikapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.rindikapp.databinding.ActivityMainBinding
import com.example.rindikapp.ui.other.AboutActivity
import com.example.rindikapp.ui.other.HowToPlayActivity
import com.example.rindikapp.ui.pick.type.TypeActivity

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Button Listener
        binding?.apply {
            btnStart.setOnClickListener {
                // Intent to PickLevelActivity
                Intent(this@MainActivity, TypeActivity::class.java).also {
                    startActivity(it)
                }
            }
            btnHowToPlay.setOnClickListener {
                // Intent to HowToPlayActivity
                Intent(this@MainActivity, HowToPlayActivity::class.java).also {
                    startActivity(it)
                }
            }
            btnAbout.setOnClickListener {
                // Intent to AboutActivity
                Intent(this@MainActivity, AboutActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}