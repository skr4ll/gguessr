package com.example.gguessr.util

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

class Utils(){
    object CalculateScore {
        fun distanceInKm(from: LatLng, to: LatLng): Double {
            val earthRadius = 6371.0 // km

            val dLat = Math.toRadians(to.latitude - from.latitude)
            val dLon = Math.toRadians(to.longitude - from.longitude)

            val lat1 = Math.toRadians(from.latitude)
            val lat2 = Math.toRadians(to.latitude)

            val a = sin(dLat / 2).pow(2.0) +
                    sin(dLon / 2).pow(2.0) * cos(lat1) * cos(lat2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return earthRadius * c
        }

        fun calculateScore(guess: LatLng, actual: LatLng): Int {
            val distance = distanceInKm(guess, actual)

            val maxScore = 5000
            val steepness = 0.001  // bestimmt, wie stark der Score abfällt

            // Sigmoid-artige Kurve
            val score = (maxScore / (1 + steepness * distance * distance)).toInt()

            return score.coerceAtLeast(0)
        }
    }
}