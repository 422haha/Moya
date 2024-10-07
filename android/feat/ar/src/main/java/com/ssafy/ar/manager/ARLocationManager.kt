package com.ssafy.ar.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.ssafy.ar.data.LocationPriority
import com.ssafy.ar.data.QuestInfo
import com.ssafy.ar.data.NearestNPCInfo
import com.ssafy.ar.data.QuestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ARLocationManager(
    private val context: Context,
    private var fusedLocationClient: FusedLocationProviderClient) {

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    private var currentPriority: Int = Priority.PRIORITY_PASSIVE

    private var locationCallback: LocationCallback? = null

    // 위치 추적 시작
    fun startLocationUpdates() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    _currentLocation.value = location
                }
            }
        }

        setFusedLocationClient(100f)
    }

    fun setFusedLocationClient(distance: Float) {
        if (context.checkLocationPermission()) {
            val newPriority = getPriorityBasedOnDistance(distance).priority
            if(currentPriority != newPriority) {
                currentPriority = newPriority

                val info = createLocationRequest(distance)
                fusedLocationClient.removeLocationUpdates(locationCallback!!)
                fusedLocationClient.requestLocationUpdates(
                    info,
                    locationCallback!!,
                    Looper.getMainLooper()
                )
            }
        }
    }

    private fun createLocationRequest(distance: Float): LocationRequest {
        val info = getPriorityBasedOnDistance(distance)

        return LocationRequest.Builder(info.priority, info.millisecond)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(info.millisecond)
            .setMinUpdateDistanceMeters(info.distance)
            .build()
    }

    // 노드와 떨어진 거리마다 우선순위 적용
    private fun getPriorityBasedOnDistance(distance: Float): LocationPriority {
        return when {
            (distance <= 100) -> LocationPriority(Priority.PRIORITY_HIGH_ACCURACY, 0f, 2000)
            (distance <= 1000) -> LocationPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 3f, 5000)
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

    fun operateNearestNPC(location: Location, questInfos: Map<Long, QuestInfo>): NearestNPCInfo {
        val npc = findNearestNPC(location, questInfos)
        val distance = npc?.let { measureNearestNpcDistance(location, it) }
        val isAvailable = isAvailableNearestNPC(distance, location)

        return NearestNPCInfo(npc, distance, isAvailable)
    }

    // 가장 가까운 노드와의 거리를 측정
    private fun measureNearestNpcDistance(location: Location, npcLocation: QuestInfo): Float {
        val targetLocation = Location("target").apply {
            latitude = npcLocation.latitude
            longitude = npcLocation.longitude
        }

        return location.distanceTo(targetLocation)
    }

    // 가장 가까운 노드를 찾기
    private fun findNearestNPC(
        currentLocation: Location,
        questInfos: Map<Long, QuestInfo>
    ): QuestInfo? {
        return questInfos.values
            .filter { it.isComplete != QuestState.COMPLETE }
            .minByOrNull { location ->
            val npcLocation = Location("npc").apply {
                latitude = location.latitude
                longitude = location.longitude
            }

            currentLocation.distanceTo(npcLocation)
        }
    }

    // 배치 가능한 거리인지 확인
    private fun isAvailableNearestNPC(
        nearestDistance: Float?,
        location: Location?
    ): Boolean {
        return ((location?.accuracy ?: 100.0f) <= 15f
                && (nearestDistance ?: 100f) <= 10f)
    }

    private fun Context.checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
