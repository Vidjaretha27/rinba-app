package com.example.rindikapp.ui.pick.level

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.rindikapp.R
import com.example.rindikapp.databinding.ActivityLevelBinding
import com.example.rindikapp.ui.play.PlayActivity
import com.example.rindikapp.utils.Constants.ADVANCED
import com.example.rindikapp.utils.Constants.BEGINNER
import com.example.rindikapp.utils.Constants.LEVEL_1
import com.example.rindikapp.utils.Constants.LEVEL_2
import com.example.rindikapp.utils.Constants.LEVEL_3
import com.example.rindikapp.utils.Constants.LEVEL_4

class LevelActivity : AppCompatActivity() {

    private var _binding: ActivityLevelBinding? = null
    private val binding get() = _binding

    private var type: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Get level type from intent
        type = intent?.getIntExtra(EXTRA_LEVEL_TYPE, 1)

        if (type != null) {
            when (type) {
                BEGINNER -> {
                    binding?.tvLevelTitle?.text = getString(R.string.level_beginner)
                }
                ADVANCED -> {
                    binding?.tvLevelTitle?.text = getString(R.string.level_advanced)
                    // Hide level 3-5 button
                    binding?.btnLevel3?.visibility = View.GONE
                    binding?.btnLevel4?.visibility = View.GONE
                }
            }
        }

        // Intent to PlayActivity
        val intent = Intent(this, PlayActivity::class.java).also {
            it.putExtra(PlayActivity.EXTRA_LEVEL_TYPE, type)
        }

        // Button Listener
        binding?.btnLevel1?.setOnClickListener {
            intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_1)
            startActivity(intent)
        }
        binding?.btnLevel2?.setOnClickListener {
            intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_2)
            startActivity(intent)
        }
        binding?.btnLevel3?.setOnClickListener {
            intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_3)
            startActivity(intent)
        }
        binding?.btnLevel4?.setOnClickListener {
            intent.putExtra(PlayActivity.EXTRA_LEVEL, LEVEL_4)
            startActivity(intent)
        }
        binding?.btnBack?.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_LEVEL_TYPE = "extra_level_type"
    }
}