package me.raghu.opentelsdk

import io.opentelemetry.api.trace.Tracer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EventCollector {

    suspend fun handleEvent(parentSpanName: String, childSpanName: String) {
        val tracer: Tracer = OpenTelemetry.tracer("android_demo_app","1.0.0")!!

        withContext(Dispatchers.IO) {
            val parentEvent = ConfigFileParser.findEvent(parentSpanName)
            if (parentEvent != null) {
                SpanManager.parentSpan(tracer, parentEvent.eventName, childSpanName)
            }
        }
    }
}