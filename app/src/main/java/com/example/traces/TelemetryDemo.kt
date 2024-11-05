package com.example.traces

import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer

class TelemetryDemo {
    companion object {
        private var tracer: Tracer? = null

        @JvmStatic
        fun main(args: Array<String>) {
            OpenTelemetryUtil.init()
            tracer = OpenTelemetryUtil.getTracer()

            // Create a span
            val span: Span = tracer!!.spanBuilder("sample-span")
                .setAttribute("key", "value")
                .startSpan()

            // Corrected addEvent
            val eventAttributes = Attributes.of(
                AttributeKey.stringKey("eventKey"), "eventValue"
            )
            span.addEvent("Sample Event", eventAttributes)

            // Simulate an error
            try {
                throw RuntimeException("This is a test exception")
            } catch (e: Exception) {
                span.setStatus(StatusCode.ERROR, "Test Exception Occurred")
                span.recordException(e)
            }

            // End the span
            span.end()
        }
    }
}
