package com.example.rindikapp.ui.score

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.rindikapp.R
import com.example.rindikapp.databinding.FragmentScoreDialogBinding
import com.example.rindikapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreDialogFragment : DialogFragment() {

    private var _binding: FragmentScoreDialogBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentScoreDialogBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get score bundle data
        val score = arguments?.getInt(EXTRA_SCORE, 0)

        // Set result score text
        binding?.tvScore?.text = getString(R.string.score_template, score)

        // Button Listener
        binding?.btnOk?.setOnClickListener {
            Intent(activity, MainActivity::class.java).also {
                activity?.startActivity(it)
            }
            activity?.finish()
        }

        return binding?.root
    }

    override fun onResume() {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_rectangle)
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_SCORE = "extra_score"
    }
}