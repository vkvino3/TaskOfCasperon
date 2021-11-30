package com.example.task.helper

import com.example.task.DataResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST("v2/api/user/activity")
    fun getDataActivity(@Header("Auth") userToken: String) : Call<DataResponse>
}