import android.Manifest
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wanderer.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun Steps(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Zmienna stanu dla liczby kroków (używamy remember, aby stan był zachowany w komponencie)
    val counter = remember { MutableStateFlow(0) }
    val stepImages = remember { mutableStateListOf<Pair<Float, Float>>() }
    val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var lastX = 0f
    var lastY = 0f
    var lastZ = 0f

    val threshold = 10f

    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    // Akceleracja w osi X, Y, Z
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    // Obliczanie zmiany w przyspieszeniu
                    val deltaX = x - lastX
                    val deltaY = y - lastY
                    val deltaZ = z - lastZ

                    // Obliczanie wartości zmiany przyspieszenia (tzw. "magnitude")
                    val magnitude = sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)

                    // Jeśli zmiana przyspieszenia jest większa niż próg, uznajemy to za krok
                    if (magnitude > threshold) {
                        counter.value = counter.value + 1  // Aktualizacja liczby kroków
                        // Dodajemy stopę w losowym miejscu
                        stepImages.add(Pair(Random.nextFloat(), Random.nextFloat()))
                        println("Step detected!")
                        println("${counter.value}")
                    }

                    // Przechowujemy aktualne wartości przyspieszenia
                    lastX = x
                    lastY = y
                    lastZ = z
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    // Sprawdzamy uprawnienia
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            )

            if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Rejestrujemy nasłuch na sensorze, jeśli pozwolenie zostało przyznane
                sensorManager.registerListener(
                    sensorEventListener,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_UI
                )
            } else {
                // Jeśli pozwolenie nie zostało przyznane, zapytaj o nie
                ActivityCompat.requestPermissions(
                    context as android.app.Activity,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    1001
                )
            }
        }
    }

    // Usuwamy nasłuch na sensorze, gdy komponent jest niszczony
    DisposableEffect(Unit) {
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    // Zmienna, która będzie przechowywała aktualny stan liczby kroków
    val stepCount by counter.collectAsState()

    // Wyświetlenie liczby kroków
    Column(modifier = modifier.fillMaxSize()) {
        // Tekst u góry
        Text(
            text = "Steps taken: $stepCount",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Kontener z stopami
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {

            stepImages.forEach { position ->
                // Przekształcenie współrzędnych 0..1 na współrzędne ekranu
                val xPos = (position.first * 1000).coerceIn(0f, 1000f)  // Skalowanie pozycji X
                val yPos = (position.second * 1000).coerceIn(0f, 1000f)  // Skalowanie pozycji Y w dolnej połowie ekranu


                val footImage: Painter = painterResource(id = R.drawable.foot)

                // Losowanie koloru z palety colorScheme
                val colorPalette = MaterialTheme.colorScheme
                val randomColor = listOf(
                    colorPalette.primary,
                    colorPalette.secondary,
                    colorPalette.tertiary,
                    colorPalette.onSurface
                ).random()  // Losowanie jednego koloru

                // Rysowanie stopę z losowym kolorem
                Image(
                    painter = footImage,
                    contentDescription = "Foot step",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = xPos.dp, y = yPos.dp)  // Losowe pozycjonowanie w dolnej części ekranu
                        .size(100.dp),  // Ustalony rozmiar stop
                    colorFilter = ColorFilter.tint(randomColor)  // Używamy losowego koloru z palety
                )
            }
        }
    }
}
