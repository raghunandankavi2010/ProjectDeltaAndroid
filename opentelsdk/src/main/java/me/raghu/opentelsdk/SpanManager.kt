package me.raghu.opentelsdk

import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context


object SpanManager {
    private val spanMap: MutableMap<String, Span> = mutableMapOf()

    fun startSpan(tracer: Tracer, spanName: String, parentSpan: Span? = null): Span {
        return spanMap[spanName] ?: run {
            val spanBuilder = tracer.spanBuilder(spanName)
                .setSpanKind(SpanKind.INTERNAL)
            parentSpan?.let { spanBuilder.setParent(Context.current().with(it)) }
            val span = spanBuilder.startSpan()
            spanMap[spanName] = span
            span
        }
    }


    fun getSpan(spanName: String): Span? {
        return spanMap[spanName]
    }

    fun parentSpan(tracer: Tracer, parentSpanName: String, childSpanName: String) {

        val spanExists = getSpan(parentSpanName)
        val span = if (spanExists != null) {
            spanExists
        } else {
            val span = tracer.spanBuilder(parentSpanName).startSpan()
            span
        }

        if (!childSpanName.isBlank()) {
            try {

                span?.makeCurrent().use { scope ->
                    println("TraceId ${span?.spanContext?.traceId}")
                    println("Parent SpanId ${span?.spanContext?.spanId}")
                    childSpan(tracer, childSpanName)
                }

            } finally {
                span.end()
            }
       } else {
         span.end()
        }

    }


    fun childSpan(tracer: Tracer, childSpanName: String) {

        val span = tracer.spanBuilder(childSpanName).startSpan()
        try {
            span.makeCurrent().use { scope ->
                println("TraceId ${span?.spanContext?.traceId}")
                println("Child SpanId ${span?.spanContext?.spanId}")
            }
        } finally {
            span.end()
        }
    }

    fun endSpan(spanName: String) {
        spanMap[spanName]?.end()
        spanMap.remove(spanName)
    }

    fun createChildOrParentSpan(tracer: Tracer, parentSpanName: String, childSpanName: String): Span {
        val parentSpan = getSpan(parentSpanName)

        return if (parentSpan != null) {
            // Create child span
            val childSpan = startSpan(tracer, childSpanName, parentSpan)
            childSpan
        } else {
            // Create parent span
            val parentSpan = startSpan(tracer, parentSpanName)
            parentSpan
        }
    }
}