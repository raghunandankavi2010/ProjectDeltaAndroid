package me.raghu.opentelsdk


import com.google.gson.annotations.SerializedName

data class EventsWrapper (
    @SerializedName("events" ) var events : ArrayList<Event> = arrayListOf()
)


data class Event (
    @SerializedName("event_name" ) var eventName : String,
    @SerializedName("event_type" ) var eventType : String
)