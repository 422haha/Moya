package com.ssafy.ar.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.ssafy.ar.data.NPCLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ARLocationManager(
    private val context: Context,
    private var fusedLocationClient: FusedLocationProviderClient) {

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    private var locationCallback: LocationCallback? = null

    // 위치 추적 시작
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(1000)
            .setMinUpdateDistanceMeters(0f)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    _currentLocation.value = location
                }
            }
        }

        if (context.checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        }
    }

    // 위치 추적 종료
    fun stopLocationUpdates() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
        }
        locationCallback = null
    }

    // 가장 가까운 노드와의 거리를 측정
    fun measureNearestNpcDistance(location: Location, npcLocation: NPCLocation): Float {
        val targetLocation = Location("target").apply {
            latitude = npcLocation.latLng.latitude
            longitude = npcLocation.latLng.longitude
        }

        return location.distanceTo(targetLocation)
    }

    // 가장 가까운 노드를 찾기
    fun findNearestNPC(
        currentLocation: Location,
        npcLocations: Map<String, NPCLocation>
    ): NPCLocation? {
        return npcLocations.values
            .filter { !it.isPlace }
            .minByOrNull { location ->
            val npcLocation = Location("npc").apply {
                latitude = location.latLng.latitude
                longitude = location.latLng.longitude
            }

            currentLocation.distanceTo(npcLocation)
        }
    }

    // 배치 가능한 거리인지 확인
    fun isAvailableNearestNPC(
        nearestDistance: Float?,
        location: Location?
    ): Boolean {
        return ((location?.accuracy ?: 100.0f) <= 30f
                && (nearestDistance ?: 100f) <= 30f)
    }

    private fun Context.checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
