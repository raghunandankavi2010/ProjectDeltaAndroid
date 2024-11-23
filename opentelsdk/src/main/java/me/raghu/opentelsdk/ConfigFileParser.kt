package me.raghu.opentelsdk

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object ConfigFileParser {

    private val eventsList = mutableListOf<Event>()

    fun parseJsonData(applicationContext: Context) {
        eventsList.addAll(
            parseJson(
                loadJSONFromAsset(applicationContext,"config.json")
            )
        )
    }

    fun loadJSONFromAsset(context: Context, fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }
    }

    fun parseJson(json: String): List<Event> {
        val gson = Gson()
        val type = object : TypeToken<EventsWrapper>() {}.type
        val eventsWrapper: EventsWrapper = gson.fromJson(json, type)
        return eventsWrapper.events
    }

    fun findEvent(eventName: String): Event? {
        val event = eventsList.find { it.eventName == eventName }
        return event
    }

    fun containsEventWithName(eventName: String): Boolean {
        return eventsList.any { it.eventName == eventName }
    }

}