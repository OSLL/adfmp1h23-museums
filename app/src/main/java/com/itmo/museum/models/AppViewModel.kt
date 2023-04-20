package com.itmo.museum.models

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppUiState(
            user = null,
            lastKnownLocation = null,
            visitedMuseums = persistentSetOf()
        )
    )
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun setUser(user: User) {
        _uiState.update { currentState ->
            currentState.copy(
                user = user
            )
        }
    }

    fun setAnonymousUser() {
        setUser(User.Anonymous)
    }

    fun addVisitedMuseum(museum: Museum) {
        _uiState.update { currentState ->
            currentState.copy(
                visitedMuseums = currentState.visitedMuseums.add(museum)
            )
        }
    }

    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        lastKnownLocation = task.result,
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.e("loc", "Failed to get device location")
        }
    }
}

data class AppUiState(
    val user: User?,
    val lastKnownLocation: Location?,
    val visitedMuseums: PersistentSet<Museum>
)