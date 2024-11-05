package com.example.traces

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.logging.LoggingSpanExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes

class OpenTelemetryUtil {
    companion object {
        @JvmStatic
        fun init() {
            val otelResource = Resource.getDefault().merge(
                Resource.create(
                    Attributes.of(
                        ResourceAttributes.SERVICE_NAME, "kotlintest",
                        ResourceAttributes.HOST_NAME, "kotlintest"
                    )
                )
            )

            val sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
                .addSpanProcessor(
                    BatchSpanProcessor.builder(
                        OtlpGrpcSpanExporter.builder()
                            .setEndpoint("https://ingest.in.signoz.cloud:443/v1/traces")
                            .addHeader("signoz-access-token", "c5d49b77-781b-4159-9394-e59b847c349e")
                            .build()
                    ).build()
                )
                .setResource(otelResource)
                .build()

            val openTelemetry: OpenTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal()

            tracer = openTelemetry.getTracer("android-tracer", "1.0.0")
        }

        private var tracer: Tracer? = null

        @JvmStatic
        fun getTracer(): Tracer? {
            return tracer
        }
    }

}