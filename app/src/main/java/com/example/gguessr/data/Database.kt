package com.example.gguessr.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

    fun loginPlayer(name: String, password: String, onResult: (Boolean) -> Unit) {
        val playersRef = db.child("players")

        // Suche alle Spieler nach dem Namen ab
        playersRef.orderByChild("name").equalTo(name)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Es gibt einen Spieler mit dem Namen
                        for (child in snapshot.children) {
                            val dbPassword = child.child("password").getValue(String::class.java)
                            if (dbPassword == password) {
                                onResult(true)   // Login erfolgreich
                                return
                            }
                        }
                        onResult(false) // Name stimmt, Passwort falsch
                    } else {
                        onResult(false) // Spielername existiert nicht
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    onResult(false)
                }
            })
    }
}