package com.example.traces.repository

import com.example.traces.model.WeatherApiResponse
import com.example.traces.networking.NetworkingService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val networkingService: NetworkingService) {

    suspend fun fetchWeather(long: String, lat: String): WeatherApiResponse {
        throw Exception("Testing open tel collector")
        return networkingService.fetchWeather(lon = long, lat = lat)
    }

}