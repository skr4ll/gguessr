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
//    fun getLocations(onResult: (List<Location>) -> Unit) {
//        val locationsRef = db.child("locations")
//        locationsRef.get()
//            .addOnSuccessListener { snapshot ->
//                val dbLocations = snapshot.children.mapNotNull { child ->
//                    child.getValue(DatabaseLocation::class.java)
//                }
//                val locationsList = dbLocations.map { dbLoc ->
//                    Location(
//                        name = dbLoc.name,
//                        description = dbLoc.description,
//                        position = dbLoc.toLatLng()
//                    )
//                }
//                onResult(locationsList)
//            }
//            .addOnFailureListener {
//                onResult(emptyList())
//            }
//    }
    fun rewriteGetLocations(onResult: (List<Location>) -> Unit){
        val locationsRef = db.child("locations")
        locationsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Die Klasse DatabaseLocation enthält zwei separate float Felder für die GPS-Position
                val dblocationsList = snapshot.children.mapNotNull { child ->
                    child.getValue(DatabaseLocation::class.java)
                }
                // Beim Umwandeln in die Klasse Locations müssen wir diese beiden Felder zu einem einzigen LatLng Objekt machen
                val locationsList = dblocationsList.map { dbLoc ->
                    Location(
                        name = dbLoc.name,
                        description = dbLoc.description,
                        position = dbLoc.toLatLng()
                    )
                }
                onResult(locationsList)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("db-err", "$error")
            }
        })
    }
    fun loginPlayer(name: String, password: String, onResult: (Boolean) -> Unit) {
        val playersRef = db.child("players")
        // Suche alle Spieler nach dem Namen ab
        playersRef.orderByChild("name").equalTo(name)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Es gibt einen Spieler mit dem Namen und pw stimmt
                        // PW für diese Zwecke hier unverschlüsselt
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
    fun getHighscores(onResult: (List<Highscore>) -> Unit){
        val highscoresRef = db.child("highscores")
        // Hier: object: Erstelle eine Instanz einer anon Klasse, die das Interface ValueEventListener implementiert
        // ValueEventListener fordert die Implementierung der beiden unten stehenden Funktionen
        // Inline Schreibeweise der anon Klasse (Wird direkt im param von addValueEventListener geschrieben
        highscoresRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val highscoresList = snapshot.children.mapNotNull { child ->
                        child.getValue(Highscore::class.java)
                    }
                println("IN DER DATA DATA BAAAASE: $highscoresList")
                onResult(highscoresList)
                }
            override fun onCancelled(error: DatabaseError) {
                Log.i("db-err", "$error")
            }
        })
    }
    fun createPlayer(name: String, password: String){
        TODO("DO IT")
    }
}