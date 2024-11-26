package com.example.wanderer

import android.app.Application
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.room.util.copy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.times
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun Level(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var xTilt by remember { mutableFloatStateOf(0f) }
    var yTilt by remember { mutableFloatStateOf(0f) }

    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                xTilt = event.values[0]
                yTilt = event.values[1]
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    LaunchedEffect(Unit) {
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    DisposableEffect(Unit) {
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            HorizontalSpiritLevelBar(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                tilt = xTilt,
            )


            VerticalSpiritLevelBar(
                modifier = Modifier
                    .width(50.dp)
                    .fillMaxHeight(),
                tilt = yTilt,
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun VerticalSpiritLevelBar(
    modifier: Modifier = Modifier,
    tilt: Float
) {
    val ballSize = 40.dp
    val maxTilt = 4.5f
    val offsetAnim = remember { androidx.compose.animation.core.Animatable(0f) }

    val tiltAngle = (tilt * 10).toInt()
    val background = MaterialTheme.colorScheme.tertiary
    val ball = MaterialTheme.colorScheme.primary

    BoxWithConstraints(
        modifier = modifier
            .padding(10.dp)
            .background(Color.Transparent)
    ) {
        val density = LocalDensity.current
        val barSize = maxHeight
        val ballHeightPx = with(density) { ballSize.toPx() } // Rozmiar kulki w px

        // Obliczanie maksymalnego przesunięcia kulki
        val maxOffset = with(density) {
            (barSize.toPx() - ballHeightPx) / 2f
        }

        // Obliczanie docelowego offsetu
        val targetOffset = (tilt / maxTilt).coerceIn(-1f, 1f) * maxOffset

        LaunchedEffect(targetOffset) {
            offsetAnim.animateTo(
                targetOffset,
                animationSpec = tween(durationMillis = 100)
            )
        }

        // Tło paska
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background.copy(0.4f), RoundedCornerShape(20.dp))
        )

        // Rysowanie kulki
        Canvas(
            modifier = Modifier
                .size(ballSize)
                .offset(y = with(density) { (barSize.toPx() / 2f - offsetAnim.value).toDp() })
        ) {
            drawCircle(ball)
        }
    }

}

@Composable
fun HorizontalSpiritLevelBar(
    modifier: Modifier = Modifier,
    tilt: Float
) {
    val ballSize = 40.dp
    val maxTilt = 4.5f
    val offsetAnim = remember { androidx.compose.animation.core.Animatable(0f) }
    val background = MaterialTheme.colorScheme.tertiary
    val ball = MaterialTheme.colorScheme.primary
    val tiltAngle = (tilt * 10).toInt()

    BoxWithConstraints(
        modifier = modifier
            .padding(10.dp)
            .background(Color.Transparent)
    ) {
        val density = LocalDensity.current
        val barSize = maxWidth
        val barWidth = barSize * 0.8f
        val maxOffset = with(density) {
            (barWidth.toPx()/2f)
        }
        val targetOffset = (tilt / maxTilt).coerceIn(-1f, 1f) * maxOffset

        LaunchedEffect(targetOffset) {
            offsetAnim.animateTo(
                targetOffset,
                animationSpec = tween(durationMillis = 100)
            )
        }

        // Ustawiamy tło na pasek
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background.copy(0.4f), RoundedCornerShape(20.dp))
        )

        // Rysowanie kulki - zmieniono obliczenia pozycji
        Canvas(
            modifier = Modifier
                .size(ballSize)
                .offset(x = with(density) { (barWidth / 2f - offsetAnim.value.toDp()) })
        ) {
            drawCircle(ball)
        }
    }
}
