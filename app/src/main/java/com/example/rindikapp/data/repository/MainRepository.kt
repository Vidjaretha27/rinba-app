package com.example.rindikapp.data.repository

import com.example.rindikapp.data.model.Prediction
import com.example.rindikapp.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MainRepository {

    suspend fun predictBilah(
        expectedBilah: RequestBody,
        audio: MultipartBody.Part
    ): Resource<Prediction>
}