package com.example.gguessr.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// Datenbankklasse. Wird direkt instanziiert per object, um die Methoden überall aufrufbar zu machen
object Database {
    private val db = Firebase.database.reference
    private val highscoresRef = db.child("highscore")
    private val locationsRef = db.child("locations")
    private val proplocsRef = db.child("proplocs")

    fun getLocations(onResult: (List<Location>) -> Unit){
        locationsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Die Klasse DatabaseLocation enthält zwei separate float Felder für die GPS-Position
                val dblocationsList = snapshot.children.mapNotNull { child ->
                    child.getValue(DatabaseLocation::class.java)
                }
                // Beim Umwandeln in die Klasse Locations müssen wir diese beiden Felder zu einem einzigen LatLng Objekt machen
                val locationsList = dblocationsList.map { dbLoc ->
                    Location(
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
    fun createProposedLocation(propLoc: DatabaseLocation){
        val key = proplocsRef.push().key
        proplocsRef.child(key.toString()).setValue(propLoc)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.e("gguessr-DB", e.message.toString())
            }
    }
    fun loginPlayer(name: String, password: String, onResult: (Boolean) -> Unit) {
        val playersRef = db.child("players")
        val query = playersRef.orderByChild("name").equalTo(name)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Es gibt einen Spieler mit dem Namen und pw stimmt
                    // PW für diese Zwecke hier unverschlüsselt
                    for (child in snapshot.children) {
                        val dbPassword = child.child("password").getValue(String::class.java)
                        val id = child.key!! // Garantie, dass nioht null. Das ist so da wir sonst nicht hier wären.
                        if (dbPassword == password) {
                            LoggedInPlayer.playerId = id
                            LoggedInPlayer.playerName = name
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
    fun createPlayer(name: String, password: String){
        TODO("DO IT")
    }
    fun getHighscores(onResult: (List<Highscore>) -> Unit){
        // Hier: object: Erstelle eine Instanz einer anon Klasse, die das Interface ValueEventListener implementiert
        // ValueEventListener fordert die Implementierung der beiden unten stehenden Funktionen
        // Inline Schreibeweise der anon Klasse (Wird direkt im param von addValueEventListener geschrieben
        highscoresRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val highscoresList = snapshot.children.mapNotNull { child ->
                        child.getValue(Highscore::class.java)
                    }
                onResult(highscoresList)
                }
            override fun onCancelled(error: DatabaseError) {
                Log.i("db-err", "$error")
            }
        })
    }
    // Den Highscore eines Spielers abfragen. Einmalig bei Login verwendet
    fun getPlayersHighscore(pid: String, onResult: (Int?) -> Unit){
        val query = highscoresRef.child(pid).child("score")
        query.get()
            .addOnSuccessListener{ snapshot ->
                val currentScore = snapshot.getValue(Int::class.java)
                onResult(currentScore)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                onResult(null)
            }
    }
    // Highscore erzeugen oder updaten für eine PlayerId
    fun createOrUpdateHighscore(highscore: Highscore, pid: String){
        highscoresRef.child(pid).setValue(highscore)
            .addOnSuccessListener {
        }
            .addOnFailureListener { e ->
                Log.e("gguessr-DB", e.message.toString())
            }
    }
}