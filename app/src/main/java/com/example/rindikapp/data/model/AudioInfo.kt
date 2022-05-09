package com.example.rindikapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioInfo(
    var mimeType: String = "",
    var displayName: String = "",
    var extension: String = "",
    var size: Long = 0,
) : Parcelable
