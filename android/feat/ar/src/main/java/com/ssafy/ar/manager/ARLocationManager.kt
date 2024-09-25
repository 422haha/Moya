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
import com.ssafy.ar.data.LocationPriority
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
        val locationRequest = createLocationRequest(100f)

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

    fun updateFusedClient(distance: Float) {
        if (context.checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(distance),
                locationCallback!!,
                Looper.getMainLooper()
            )
        }
    }

    private fun createLocationRequest(distance: Float): LocationRequest {
        val info = getPriorityBasedOnDistance(distance)

        return LocationRequest.Builder(info.priority, info.millisecond)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(info.millisecond)
            .setMinUpdateDistanceMeters(info.distance)
            .build()
    }

    private fun getPriorityBasedOnDistance(distance: Float): LocationPriority {
        return when {
            (distance < 100) -> LocationPriority(Priority.PRIORITY_HIGH_ACCURACY, 2f, 3000)
            (distance < 1000) -> LocationPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5f, 10000)
            else -> LocationPriority(Priority.PRIORITY_LOW_POWER, 5f, 10000)
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
        return ((location?.accuracy ?: 100.0f) <= 10f
                && (nearestDistance ?: 100f) <= 5f)
    }

    private fun Context.checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
