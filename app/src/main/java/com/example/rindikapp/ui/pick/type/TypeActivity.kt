package com.example.rindikapp.ui.pick.type

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rindikapp.databinding.ActivityTypeBinding
import com.example.rindikapp.ui.pick.level.LevelActivity
import com.example.rindikapp.utils.Constants.ADVANCED
import com.example.rindikapp.utils.Constants.BEGINNER

class TypeActivity : AppCompatActivity() {

    private var _binding: ActivityTypeBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTypeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Button Listener
        binding?.apply {
            btnOneHandLevel.setOnClickListener {
                Intent(this@TypeActivity, LevelActivity::class.java).also {
                    it.putExtra(LevelActivity.EXTRA_LEVEL_TYPE, BEGINNER)
                    startActivity(it)
                }
            }
            btnTwoHandLevel.setOnClickListener {
                Intent(this@TypeActivity, LevelActivity::class.java).also {
                    it.putExtra(LevelActivity.EXTRA_LEVEL_TYPE, ADVANCED)
                    startActivity(it)
                }
            }
            btnBack.setOnClickListener { finish() }
        }
    }
}