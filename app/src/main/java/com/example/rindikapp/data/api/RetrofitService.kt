package com.example.rindikapp.data.api

import com.example.rindikapp.data.model.Wrapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @Multipart
    @POST("predict-bilah")
    suspend fun predictBilah(
        @Part("expected_bilah") expectedBilah: RequestBody,
        @Part audio: MultipartBody.Part
    ): Response<Wrapper>
}