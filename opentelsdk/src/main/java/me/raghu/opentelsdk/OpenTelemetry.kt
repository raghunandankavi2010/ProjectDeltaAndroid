package me.raghu.opentelsdk

import android.app.Application
import android.util.Log
import io.opentelemetry.android.OpenTelemetryRum
import io.opentelemetry.android.OpenTelemetryRumBuilder
import io.opentelemetry.android.config.OtelRumConfig
import io.opentelemetry.android.features.diskbuffering.DiskBufferingConfiguration
import io.opentelemetry.api.common.AttributeKey.stringKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.incubator.events.EventBuilder
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import io.opentelemetry.sdk.logs.internal.SdkEventLoggerProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


object OpenTelemetry {

    const val TAG = "otel.demo"

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun initialize(
        application: Application,
        spansIngestUrl: String,
        logsIngestUtl: String,
        accessToken: String,
        serviceName: String
    ) {

        Log.i(TAG, "Initializing the opentelemetry-android-agent")
        val diskBufferingConfig =
            DiskBufferingConfiguration.builder()
                .setEnabled(true)
                .setMaxCacheSize(10_000_000)
                .build()
        val config =
            OtelRumConfig()
                .setGlobalAttributes(
                    Attributes.of(
                        stringKey("service.name"), serviceName
                    )
                )
                .setDiskBufferingConfiguration(diskBufferingConfig)

        val spansIngestUrl = spansIngestUrl
        //"https://ingest.in.signoz.cloud:443/v1/traces" //"http://3.7.46.28:4317"
        val logsIngestUrl = logsIngestUtl
        // "https://ingest.in.signoz.cloud:443/v1/traces" // "http://3.7.46.28:4317"
        val otelRumBuilder: OpenTelemetryRumBuilder =
            OpenTelemetryRum.builder(application, config)
                .addSpanExporterCustomizer {
                    OtlpHttpSpanExporter.builder()
                        .setEndpoint(spansIngestUrl)
                        .addHeader(
                            "signoz-access-token",
                            accessToken
                        )
                        .build()
                }
                .addLogRecordExporterCustomizer {
                    OtlpHttpLogRecordExporter.builder()
                        .setEndpoint(logsIngestUrl)
                        .addHeader(
                            "signoz-access-token",
                            accessToken
                        )
                        .build()

                }
        try {
            rum = otelRumBuilder.build()
            Log.d(TAG, "RUM session started: " + rum!!.rumSessionId)
        } catch (e: Exception) {
            Log.e(TAG, "Oh no!", e)
        }

        // Call your JSON reading function here
        val job = scope.launch {
            ConfigFileParser.parseJsonData(application)
        }

        scope.launch {
            job.join()
            scope.cancel()
        }
    }

    var rum: OpenTelemetryRum? = null

    fun tracer(name: String, version: String): Tracer? {
        return rum?.openTelemetry?.getTracer(name, version)
    }

    fun eventBuilder(scopeName: String, eventName: String): EventBuilder {
        val loggerProvider = rum?.openTelemetry?.logsBridge
        val eventLogger =
            SdkEventLoggerProvider.create(loggerProvider).get(scopeName)
        return eventLogger.builder(eventName)
    }

}
