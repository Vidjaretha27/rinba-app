package com.example.rindikapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.rindikapp.R

class BilahImageView(
    private val ctx: Context,
    attr: AttributeSet
) : AppCompatImageView(ctx, attr) {

    fun highlight() {
        setBackgroundColor(ContextCompat.getColor(ctx, R.color.bar_atas))
    }

    fun removeHighlight() {
        setBackgroundColor(ContextCompat.getColor(ctx, R.color.card))
    }
}