package com.example.traces

import android.app.Application
import me.raghu.opentelsdk.OpenTelemetry

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        OpenTelemetry.initialize(this,
            "https://ingest.in.signoz.cloud:443/v1/traces",
            "https://ingest.in.signoz.cloud:443/v1/traces",
            "c5d49b77-781b-4159-9394-e59b847c349e",
            "test"
        )
    }
}