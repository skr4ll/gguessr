package com.example.gguessr.data

import com.google.android.gms.maps.model.LatLng

data class Location(
    val name: String,
    val description: String,
    val position: LatLng
)