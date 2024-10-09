package com.skele.moya.background.di

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 걸음 수 측정을 위한 SensorManager
 * Manifest.permission.ACTIVITY_RECOGNITION 이 필요함
 */
@Singleton
class StepManager @Inject constructor() {
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private lateinit var sensorEventListener: SensorEventListener

    private var initialStepCount = 0
    var stepCount = 0
        private set
    var isCounting = false
        private set

    fun initializeStepSensor(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        sensorEventListener =
            object : SensorEventListener {
                override fun onAccuracyChanged(
                    sensor: Sensor?,
                    accuracy: Int,
                ) {}

                override fun onSensorChanged(event: SensorEvent) {
                    if (initialStepCount > 0) {
                        stepCount = event.values[0].toInt() - initialStepCount
                    } else {
                        initialStepCount = event.values[0].toInt()
                    }
                }
            }
    }

    fun startCounting(){
        if(isCounting) return

        stepCounterSensor?.let {
            isCounting = true
            initialStepCount = 0

            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
            )
        }
    }

    fun disposeStepSensor() {
        if(isCounting){
            sensorManager.unregisterListener(sensorEventListener)
            isCounting = false
        }
    }
}