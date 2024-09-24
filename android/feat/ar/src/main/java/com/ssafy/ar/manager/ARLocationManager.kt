package com.ssafy.ar.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.ssafy.ar.data.NPCLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ARLocationManager(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    private var locationCallback: LocationCallback? = null

    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(1000)
            .setMinUpdateDistanceMeters(0f)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    coroutineScope.launch {
                        _currentLocation.emit(location)
                    }
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
        return npcLocations.values.minByOrNull { location ->
            val npcLocation = Location("npc").apply {
                latitude = location.latLng.latitude
                longitude = location.latLng.longitude
            }

            currentLocation.distanceTo(npcLocation)
        }
    }

    private fun Context.checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

//private var locationUpdateJob: Job? = null
//private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
//// 위치 추적 시작
//fun startLocationUpdates() {
//    coroutineScope.launch {
//        while (true) {
//            val location = getCurrentLocation()
//
//            location?.let {
//                _currentLocation.emit(it)
//            }
//            delay(2000) // 2초 대기
//        }
//    }
//}
//
//private suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
//    if (!context.checkLocationPermission()) {
//        continuation.resume(null)
//        return@suspendCancellableCoroutine
//    }
//
//    val locationRequest = createCurrentLocationRequest(1000, 0)
//
//    fusedLocationClient.getCurrentLocation(locationRequest, createCancellationToken())
//        .addOnSuccessListener { location ->
//            continuation.resume(location)
//        }
//        .addOnFailureListener { exception ->
//            continuation.resumeWithException(exception)
//        }
//}
//
//// 위치 추적 종료
//fun stopLocationUpdates() {
//    locationUpdateJob?.cancel()
//    locationUpdateJob = null
//    _currentLocation.value = null
//}
//
//private fun createCancellationToken(): CancellationToken {
//    val cancellationTokenSource = CancellationTokenSource()
//    return cancellationTokenSource.token
//}
//
//private fun createCurrentLocationRequest(limitTime: Long, cachingExpiresIn: Long): CurrentLocationRequest =
//    CurrentLocationRequest.Builder()
//        .setDurationMillis(limitTime)
//        .setMaxUpdateAgeMillis(cachingExpiresIn)
//        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//        .build()
