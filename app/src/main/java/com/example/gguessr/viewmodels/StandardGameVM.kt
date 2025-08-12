package com.example.gguessr.viewmodels

import androidx.lifecycle.ViewModel

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.streetview.StreetViewCameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StandardGameVM () : ViewModel()
{

    private val _position = MutableStateFlow(LatLng(48.8584, 2.2945)) // Eiffelturm
    val position = _position.asStateFlow()

    private val _cameraPositionState = MutableStateFlow(StreetViewCameraPositionState())
    val cameraPositionState = _cameraPositionState.asStateFlow()

    fun updatePosition(latLng: LatLng) {
        _position.value = latLng
    }

}