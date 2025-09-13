package com.example.gguessr.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gguessr.data.Database
import com.example.gguessr.data.DatabaseLocation

class ProposeLocVM : ViewModel() {
    fun proposeLoc(desc: String, lat: Double, lon: Double){
        val proploc = DatabaseLocation(desc, lat, lon)
        Database.createProposedLocation(proploc)
    }

}
