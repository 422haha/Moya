package com.skele.moya.background.di

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.ssafy.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationManager
    @Inject
    constructor() {
        private lateinit var fusedsLocationClient: FusedLocationProviderClient
        private lateinit var locationCallback: LocationCallback
        private lateinit var locationRequest: LocationRequest
        var isTracking = false
            private set

        private val locationList = mutableListOf<LatLng>()

        fun initialize(context: Context) {
            fusedsLocationClient = LocationServices.getFusedLocationProviderClient(context)
            locationCallback =
                object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        result.lastLocation?.let { location ->
                            val latLng = LatLng(location.latitude, location.longitude)
                            locationList.add(latLng)
                        }
                    }
                }
            locationRequest = LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 10000).build()
        }

        fun startTracking(context: Context) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (!isTracking) {
                    isTracking = true

                    locationList.clear()

                    fusedsLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null,
                    )
                }
            }
        }

        fun stopTracking() {
            if (isTracking) {
                isTracking = false
                fusedsLocationClient.removeLocationUpdates(locationCallback)
            }
        }

        fun getLocationList(): List<LatLng> = locationList.toList()
    }
