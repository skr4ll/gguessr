package com.example.gguessr.data

import com.google.android.gms.maps.model.LatLng

// Datenklasse zur Verwendung im Quellcode. Wird aus unterer Datenklasse erzeugt
data class Location(
    val name: String,
    val description: String,
    val position: LatLng
)

// Datenklassse, die dem Format in der Datenbank entspricht (Firbase Realtime Database im JSON-Format)
data class DatabaseLocation(
    val name: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    fun toLatLng() = LatLng(latitude, longitude)
}