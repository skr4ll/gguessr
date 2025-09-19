# Abgabe zum Seminar BSI-2  


# Technische Beschreibung GGuessr  --- https://github.com/skr4ll/gguessr
Die vorliegende App fusst im wesentlichen auf der Implementierung der Spiellogik im ViewModel (VM): StandardGameVM.kt und der zugehörigen View: StandardGameView.kt. 
Im Zusammenspiel mit mehreren Objekten, wird die Grundfunktionalität eines GeoGuesserartigen Spiels bereitgestellt.  Diese Logik und Objekte sind um das Google Maps SDK for Android (Maps SDK) herum konzipiert.

## Spiellogik, deren View und Objekte

### Location.kt: Datenklassen und LatLng-Klasse
Enthält zwei Datenklassen, die Locations abbilden: Location und DataBaseLocation. Diese Trennung ist notwendig, da die Daten zur Verwendung im Code der App von anderer Form sein müssen. Zur Verwendung von Koordinaten wird die Klasse LatLng aus dem Maps SDK verwendet. 
Die Objekte dieser Klasse bündeln ein Koordinatenpaar von Breiten- und Längengrad. Dieses Objekt ist essenziell für alle weitern Methoden des SDK. Bei DatabaseLocation werden die Koordinaten separat gespeichert. Bei Location gebündelt als LatLng.

### StandardGameVM.kt
Stellt eine enum Klasse für die verschiedenen Phasen eines Spiels bereit. Diese werden in Methoden genutzt um die jeweils notwendigen Views (StreetView zum Umschauen, Maps zum Raten) anzeigen zu lassen.
Ausgehend von einer MutableList von Locations, die aus der Datenbank (s.u.) kopiert werden, wird das Spiel initialisiert. Zwei globale Variablen bestimmen, den Spielmodus der vorliegt: rankedGameStarted und timedGameStarted (siehe Objekt LoggedInPlayer). Sind beide dieser Variablen false befindet sich das ViewModel im Modus "Lokales Spiel". Sonst analog dazu "Ranked" oder "Zeitlimit". 

#### Modus: "Lokales Spiel"
Aus der Locationlist wird eine Location per zufällig generiertem Index ausgewählt. Das Spiel befindet sich in der Phase "StreetView" an dieser Location. Der Spieler interagiert nun mit der StandardGameView und beeinflusst die momentane Spielphase. Zum Phasenwechsel stellt das VM diverse Funktionen bereit.
Wechselt die Phase in "Guessing" kann die geratene Location gesetzt werden (setGuess()). Wurde diese bestätigt, wird submitGuess() aufgerufen. Hier wird die Helferfunktion zur Punkteberechnung (s.u.) aufgerufen, die State Variablen für das UI entsprechend geupdated und die Phase auf "Result" gesetzt.
Dies stellt eine einzelne Runde dar. Wird nun aus der View die Funktion nextRound() aufgerufen, wird die letzte Location aus der List entfernt und eine neue Location zum raten zufällig ausgewählt. Die Funktion prüft auch, ob es sich um die letzte Runde gehandelt hat. Wenn das der Fall ist, wird die Phase auf "End" gesetzt.

#### Modus: "Ranked"
Die Besonderheit hierbei ist, das die erzielten Punkte des Spielers in der Datenbank (DB) erfasst werden. Hierzu dient die Funktion compareAndUpdateHighscore(), die in 
der Phase "End" aufgerufen wird. Hat der Spieler noch keinen Highscore in der DB oder ist der neue Highscore höher als der Vorhandene, wird dieser gesetzt.

#### Modus "Zeitlimit"
Hierbei wird zusätzlich ein timer aktiviert und überwacht. Nach Ablauf des Timers wechselt die Phase sofort auf "End" und es werden die bis dahin erreichten Punkte gewertet.

### StandardGameView.kt
In der TopBar werden die relevanten State Variablen aus dem VM dargestellt. Je nach momenaner GamePhase werden die unterschiedlichen Composable Komponenten aus dem Maps SDK dargestellt. Die beiden grundlegenden sind StreetView und GoogleMap. Der StreetView wird die im VM gesetzt zufällige Position übergeben. Wird nun der Button "Ort tippen" getappt, wird startGuessing() aufgerufen (Phasenwechsel auf "Guessing"), das UI aktualisiert sich und ein GoogleMap Composable wird angezeigt.
Diese bietet den Parameter onMapClick, über welchen bei Tap auf die Map per setGuess() der State der aktuellen guess Variable auf das LatLng-Objekt mit den Koordinatendes getappten Punktes gesetzt werden. Über das Composable "Marker", wird für diese Position eine Markierung angezeigt. Äquivalent wird dann bei Bestätigung eine neue Karte angezeigt, die sowohl den Marker für den guess wie auch den für die wahre Position enthält, sowie eine Verbindungslinie (Polyline) zwischen beiden.
War dies die letzte Runde wird nun eine Zusammenfassung angezeigt und man kann zurück oder nochmal spielen.

## Datenbank, Spieler und Highscores

Damit Spieler und Highscores verknüpft werden können, wurde eine simple Login und Registrierungslogik implementiert. Diese basiert auf der Google Firebase Realtime Database. Die Datenklassen Player und Highscores bilden diese Objekte im Code nach. Die Firebase DB ist JSON basiert, wobei jeder Knoten direkt unterhalb des root Knotens als eine Art Tabelle (wäre es bei SQL) angesehen werden kann. Jeder Eintrag unterhalb eines solchen Knotens enthält eine eindeutige ID und darunter die eigentlichen Felder für den jeweiligen Eintrag, z. B. root{ "players": { "-O_8n302zoq2lq8_cF7k": { "name": "c", "password": "c" }, "-O_SqyQ66Y8AdqlEZ3mt": { "name": "d", "password": "d" }} etc. Wie schon erkennbar hat das Player Objekt nur die Felder "name" und "password". Diese Objekte dienen einzig dazu einen Spieler identifizieren zu können, um die Highscores entsprechend verknüpfen zu können. Hierzu enthält ein Highscoreobjekt als ID die selbe ID wie der entsprechende Player und als Felder Namen, Punkte, Datum etc.  
Zum Lesen und Schreiben in die Datenbank wird das Firebase SDK von Google verwendet. Dieses stellt allerlei praktische Funktionen bereit um aus dem Kotlin Code heraus die anfallenden Datenbankabfragen zu bewältigen. Unter anderem können über die Methoden Kotlinobjekte direkt in das JSON für die DB geparsed werden und Vice Versa.
Die Funktionen sind im Code alle unter dem object Database implementiert und werden von den VMs genutzt. Dies stellt in der MVVM-Architektur das Model dar. Bei Login wird gegen die DB geprüft ob der Name des Spielers existiert und darüber eingeloggt. Bei Registrierung wird ebenfalls geguckt, ob ein Spieler dieses Namens bereits existiert und nur falls nicht ein neuer Spieler mit entsprechendem Namen und Passwort angelegt.  
Alle im Spiel verwendeten Locations sind in der DB gespeichert und werden von dort bezogen. Spieler können Locations vorschlagen. Diese Vorschläge werden zunächst in einer speziellen Tabelle gespeichert und müssen von einem Admin bestätigt werden, um zu normalen Locations zu werden (siehe Website).

## Loginstatus und Weiteres
### Loginstatus
Um einen Spieler als eingeloogt zu kennzeichnen wurde ein sehr simples Verfahren genutzt. Es wird aus Einfachheitsgründen ein object in der Datei Player.kt definiert: LoggedInPlayer. Wird nun aus der LoginVM ein aus der LoginView erhaltener Spieler und Passwortstring erfolgreich gegen die Datenbank verfiziert, werden die im object LoggedInPlayer entsprechend der Spielername, sowies dessen ID gesetzt. Dies dient nun dazu mögliche erzielte Highscores mit diesem Spieler zu verknüpfen (Create/Update).  
Passwörter werden nicht verschlüsselt in der Datenbank abgelegt. Dies ist für diese App in Ordnung, da hier keine sensiblen Daten vorliegen und der Zugriff auf die Datenbank sehr beschränkt über die App läuft. Für produktive Versionen sollte jedoch selbstverständlich ein anderes Loginverfahren verwendet werden. 
### Globale Variablen 
Weiterhin werden in LoggedInPlayer die globalen Variablen für den Spielmodus gehalten. Diese werden aus dem Hauptmenü heraus bei Navigation zu dem entsprechenden Spielmodus gesetzt und, bei Beendigung eines Spiels des jeweiligen Modus vom StandardGameVM wieder zurück gesetzt.
### Punkteberechnung
In der Klasse Utils finden sich die Funktionen zur Punkteberechnung. Da hierfür zunächst die Distanz zwischen geratenem und wahrem Punkt berechnet werden muss, wurde hierzu die sogennante Haversine-Formel verwendet. Diese berechnet die kürzeste Distanz zwischen zwei Punkten auf einer Kugel.  
Anhand der ermittelten Distanz werden die Punkte berechnet, indem eine Funktion verwendet wird, die bei größer werdender Distazn die erzielten Punkte immer stärker verringert.
### Website
Ergänzend zur Android App wurde eine simple Website entwickelt, die 3 Funktionen bietet: Highscores anzeigen, eine Location vorschlagen und einen Login für "Manager". Wenn man sich als Manager einloggt erhält man Zugriff auf alle vorgeschlagenen Locations. Diese können direkt auf maps.google.com geöffnet werden. Anschließend kann man alle Felder bearbeiten und die Location akzeptieren oder löschen.  
Das Repository zur Webandwendung ist auf: https://github.com/skr4ll/gguessr-web zu finden.











