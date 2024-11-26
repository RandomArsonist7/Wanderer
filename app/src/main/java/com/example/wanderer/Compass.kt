package com.example.wanderer

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Compass (viewModel: CompassViewModel){
    val azimuth by viewModel.azimuth.collectAsState()
    val color = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.compass),
            contentDescription = "Kompas",
            modifier = Modifier
                .size(380.dp)
                .graphicsLayer {
                    rotationZ = -azimuth
                },
            colorFilter = ColorFilter.tint(color)
        )
    }
}


class CompassViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {
    private val sensorManager = application.getSystemService(SensorManager::class.java)
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val _azimuth = MutableStateFlow(0f)
    val azimuth: StateFlow<Float> = _azimuth

    init {
        if (accelerometer == null) Log.e("Compass", "Accelerometer not available!")
        if (magnetometer == null) Log.e("Compass", "Magnetometer not available!")
        startListening()
    }

    private fun startListening() {
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
            Sensor.TYPE_MAGNETIC_FIELD -> System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        val rotationMatrix = FloatArray(9)
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)) {
            val orientationAngles = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            _azimuth.value = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}