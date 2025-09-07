package com.example.gguessr.data

import com.google.android.gms.maps.model.LatLng

data class Location(
    val name: String,
    val description: String,
    val position: LatLng
)

data class DatabaseLocation(
    val name: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    fun toLatLng() = LatLng(latitude, longitude)
}