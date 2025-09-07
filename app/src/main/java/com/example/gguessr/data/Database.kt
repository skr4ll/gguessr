package com.example.gguessr.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.database

object Database {
    private val db = Firebase.database.reference
    // Locations aus der Datenbank erhalten (Wird vom Game-VM genutzt)
    fun getLocations(onResult: (List<Location>) -> Unit) {
        val locationsRef = db.child("locations")

        locationsRef.get()
            .addOnSuccessListener { snapshot ->
                val dbLocations = snapshot.children.mapNotNull { child ->
                    child.getValue(DatabaseLocation::class.java)
                }
                val locationsList = dbLocations.map { dbLoc ->
                    Location(
                        name = dbLoc.name,
                        description = dbLoc.description,
                        position = dbLoc.toLatLng()
                    )
                }
                println("HIER AUS DATABASEEEEEEEEEEEEEEEEEEEEE : $locationsList")
                onResult(locationsList)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}