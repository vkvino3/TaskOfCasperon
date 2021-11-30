package com.example.task

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ApiClient {


companion object{
    private var retrofit: Retrofit? = null
    fun getClient(): Retrofit {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addNetworkInterceptor { chain ->
                    var request: Request? = null
                    val original = chain.request()
                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder()
                    request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl("https://uat.evmobility.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        return retrofit!!
    }
}
}