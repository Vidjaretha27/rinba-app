package com.example.rindikapp.ui.play

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rindikapp.R
import com.example.rindikapp.databinding.ActivityPlayBinding
import com.example.rindikapp.recorder.RindikRecorder
import com.example.rindikapp.ui.other.HowToPlayActivity
import com.example.rindikapp.ui.pick.type.TypeActivity
import com.example.rindikapp.ui.score.ScoreDialogFragment
import com.example.rindikapp.utils.BilahImageView
import com.example.rindikapp.utils.Constants.ADVANCED
import com.example.rindikapp.utils.Constants.BEGINNER
import com.example.rindikapp.utils.Pattern.ADVANCED_PATTERN
import com.example.rindikapp.utils.Pattern.BEGINNER_PATTERN
import com.example.rindikapp.utils.Status
import com.example.rindikapp.viewmodel.MainViewModel
import com.github.squti.androidwaverecorder.RecorderState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val PERMISSION_REQUEST_RECORD_AUDIO = 77

@AndroidEntryPoint
class PlayActivity : AppCompatActivity() {

    private var _binding: ActivityPlayBinding? = null
    private val binding get() = _binding

    private val viewModel: MainViewModel by viewModels()

    private var type: Int? = null
    private var level: Int? = null

    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Get data type and level from intent
        type = intent?.getIntExtra(EXTRA_LEVEL_TYPE, 0)
        level = intent?.getIntExtra(EXTRA_LEVEL, 0)?.minus(1)
        Log.d("RindikPlay", "type=$type, level=$level")

        // Get level and initial highlight for Bilah in Rindik
        when (type) {
            BEGINNER -> {
                binding?.tvLevelTitle?.text = getString(R.string.level_beginner)
                val bilahImageView: BilahImageView =
                    binding?.rindikContainer?.getChildAt(BEGINNER_PATTERN[level!!][0] - 1) as BilahImageView
                bilahImageView.highlight()
            }
            ADVANCED -> {
                binding?.tvLevelTitle?.text = getString(R.string.level_advanced)
                val pattern = ADVANCED_PATTERN[level!!]
                val patternLeft = pattern[0]
                val patternRight = pattern[1]
                val bilahImageViewLeft: BilahImageView =
                    binding?.rindikContainer?.getChildAt(patternLeft[0] - 1) as BilahImageView
                bilahImageViewLeft.highlight()
                val bilahImageViewRight: BilahImageView =
                    binding?.rindikContainer?.getChildAt(patternRight[0] - 1) as BilahImageView
                bilahImageViewRight.highlight()
            }
        }

        // Rindik Recorder
        val rindikRecorder = RindikRecorder(
            this,
            {
                when (it) {
                    RecorderState.RECORDING -> startRecordingState()
                    RecorderState.STOP -> stopRecordingState()
                    else -> stopRecordingState()
                }
            }
        )

        // Observe loading state (EDIT THIS)
        val progressDialog = AlertDialog.Builder(this)
            .setView(R.layout.layout_progress_bar)
            .setCancelable(false)
            .create()
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressDialog.show()
            }
        }

        // Observe error message
        viewModel.errorMessage.observe(this) { message ->
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.error))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), { _, _ ->
                    Intent(this, TypeActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                })
                .create()
                .show()
        }

        viewModel.currentPrediction.observe(this) {
            if (it.status == Status.SUCCESS) {
                progressDialog.dismiss()
            }
        }

        // Button Listener
        binding?.apply {
            btnStartStop.setOnClickListener {
                // If not recording
                if (!isRecording) {
                    if (ContextCompat.checkSelfPermission(
                            this@PlayActivity,
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@PlayActivity,
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            PERMISSION_REQUEST_RECORD_AUDIO
                        )
                    } else {
                        rindikRecorder.startRecording()
                    }
                } else {
                    rindikRecorder.stopRecordingForResult { counter, audio ->
                        if (type == BEGINNER) {
                            beginnerLevel(counter, audio)
                        } else if (type == ADVANCED) {
                            advancedLevel(counter, audio)
                        }
                    }
                }
            }

            btnHelp.setOnClickListener {
                Intent(this@PlayActivity, HowToPlayActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startRecordingState() {
        isRecording = true
        binding?.apply {
            tvInfo.text = getString(R.string.recording)
            btnStartStop.text = getString(R.string.stop)
        }
    }

    private fun stopRecordingState() {
        isRecording = false
        binding?.apply {
            tvInfo.text = getString(R.string.stopped)
            btnStartStop.text = getString(R.string.record)
        }
    }

    private fun beginnerLevel(counter: Int, audio: File) {
        val pattern = BEGINNER_PATTERN[level!!]

        if (counter == pattern.size - 1) {
            val bundle = Bundle()
            bundle.putInt(ScoreDialogFragment.EXTRA_SCORE, viewModel.score.value!!)
            val dialog = ScoreDialogFragment()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, ScoreDialogFragment::class.java.simpleName)
            return
        }

        if (counter < pattern.size) {
            // Set highlight in Bilah
            val prevIndex = pattern[counter] - 1
            val prevBilahView =
                binding?.rindikContainer?.getChildAt(prevIndex) as BilahImageView
            prevBilahView.removeHighlight()

            if (counter != pattern.size - 1) {
                val nextIndex = pattern[counter + 1] - 1
                val nextBilahView =
                    binding?.rindikContainer?.getChildAt(nextIndex) as BilahImageView
                nextBilahView.highlight()
            }

            val bilah = pattern[counter]
            Log.d(
                "BeginnerLevel",
                "counter=$counter, bilah=$bilah, audio=${audio.name}"
            )

            val expectedBilah = "Bilah $bilah"
            val expectedBilahPart: RequestBody =
                expectedBilah.toRequestBody(MultipartBody.FORM)
            val audioRequestBody = audio.asRequestBody()
            val audioPart = MultipartBody.Part.createFormData(
                "audio",
                audio.name,
                audioRequestBody
            )
            Log.d("BeginnerLevel", "expectedBilah=$expectedBilah")
            viewModel.setCurrentPrediction(expectedBilahPart, audioPart)

        } else {
            val bundle = Bundle()
            bundle.putInt(ScoreDialogFragment.EXTRA_SCORE, viewModel.score.value!!)
            val dialog = ScoreDialogFragment()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, ScoreDialogFragment::class.java.simpleName)
        }
    }

    private fun advancedLevel(counter: Int, audio: File) {
        val pattern = ADVANCED_PATTERN[level!!]

        if (counter == pattern[0].size - 1) {
            val bundle = Bundle()
            bundle.putInt(ScoreDialogFragment.EXTRA_SCORE, viewModel.score.value!!)
            val dialog = ScoreDialogFragment()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, ScoreDialogFragment::class.java.simpleName)
            return
        }

        if (counter < pattern[0].size) {
            val patternLeft = pattern[0]
            val patternRight = pattern[1]

            val prevIndexLeft = patternLeft[counter] - 1
            val prevIndexRight = patternRight[counter] - 1
            Log.d("AdvancedLevel", "prevIndexLeft=$prevIndexLeft, prevIndexRight=$prevIndexRight")

            // Left Hand
            val prevBilahViewLeft =
                binding?.rindikContainer?.getChildAt(prevIndexLeft) as BilahImageView
            prevBilahViewLeft.removeHighlight()

            // Right Hand
            val prevBilahViewRight =
                binding?.rindikContainer?.getChildAt(prevIndexRight) as BilahImageView
            prevBilahViewRight.removeHighlight()

            if (counter != pattern[0].size - 1) {
                val nextIndexLeft = patternLeft[counter + 1] - 1
                val nextIndexRight = patternRight[counter + 1] - 1
                Log.d(
                    "AdvancedLevel",
                    "nextIndexLeft=$nextIndexLeft, nextIndexRight=$nextIndexRight"
                )

                // Left Hand
                val nextBilahViewLeft =
                    binding?.rindikContainer?.getChildAt(nextIndexLeft) as BilahImageView
                nextBilahViewLeft.highlight()

                // Right Hand
                val nextBilahViewRight =
                    binding?.rindikContainer?.getChildAt(nextIndexRight) as BilahImageView
                nextBilahViewRight.highlight()
            }

            val bilahLeft = pattern[0][counter]
            val bilahRight = pattern[1][counter]
            Log.d(
                "AdvancedLevel",
                "counter=$counter, bilah_L=$bilahLeft, bilah_R=$bilahRight, audio=${audio.name}"
            )

            val expectedBilah = "Bilah $ADVANCED.$bilahLeft.$bilahRight"
            val expectedBilahPart: RequestBody =
                expectedBilah.toRequestBody(MultipartBody.FORM)
            val audioRequestBody = audio.asRequestBody()
            val audioPart = MultipartBody.Part.createFormData(
                "audio",
                audio.name,
                audioRequestBody
            )
            Log.d("AdvancedLevel", "expectedBilah=$expectedBilah")
            viewModel.setCurrentPrediction(expectedBilahPart, audioPart)

        } else {
            val bundle = Bundle()
            bundle.putInt(ScoreDialogFragment.EXTRA_SCORE, viewModel.score.value!!)
            val dialog = ScoreDialogFragment()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, ScoreDialogFragment::class.java.simpleName)
        }
    }

    companion object {
        const val EXTRA_LEVEL_TYPE = "extra_level_type"
        const val EXTRA_LEVEL = "extra_level"
    }
}