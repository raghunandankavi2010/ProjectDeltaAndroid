package com.example.traces.networking

import com.example.traces.model.WeatherApiResponse
import com.example.traces.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkingService {

    @GET("data/2.5/forecast")
    suspend fun fetchWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = Constants.APP_ID
    ): WeatherApiResponse

}