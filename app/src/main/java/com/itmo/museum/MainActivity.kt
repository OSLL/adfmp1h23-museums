package com.itmo.museum

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.itmo.museum.models.MapViewModel
import com.itmo.museum.screens.MainScreen
import com.itmo.museum.ui.theme.MuseumTheme
import com.itmo.museum.util.makeLocationPermissionDeniedToast
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            @Suppress("UNNECESSARY_SAFE_CALL")
            val location = locationResult?.lastLocation ?: return
            mapViewModel.updateLocation(location)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                makeLocationPermissionDeniedToast(this)
            }
        }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        locationCallback.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10_000
            ).build()

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    private fun askPermissions() {
        when (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)) {
            PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }
            else -> {
                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        setContent {
            MuseumTheme {
                MainScreen(mapViewModel = mapViewModel)
            }
        }
    }
}
