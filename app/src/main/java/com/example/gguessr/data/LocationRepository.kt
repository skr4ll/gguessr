package com.example.gguessr.data

import com.google.android.gms.maps.model.LatLng

object LocationRepository {
    val locations = listOf(
        Location("Uni Köln", "Am Philosphikum", LatLng(50.92894992179465, 6.927461350615642)),
        Location("Le Grand Large", "Belle-Île-en-Mer", LatLng(47.30293581089602, -3.229245671037746)),
        Location("Kölner Dom", "Köln", LatLng(50.9413, 6.9583)),
        Location("Statue of Liberty", "New York", LatLng(40.6892, -74.0445)),
        Location("Macau Turm", "Macau", LatLng(22.187920244407167, 113.55627341623087)),

    )
}