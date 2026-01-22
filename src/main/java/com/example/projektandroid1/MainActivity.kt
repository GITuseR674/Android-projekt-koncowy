package com.example.projektandroid1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projektandroid1.ui.theme.ProjektAndroid1Theme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.sqrt
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.firestore.Query
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            ProjektAndroid1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF81D4FA), Color(0xFFFE9FD8))
                                )
                            )
                            .padding(innerPadding)
                    ) {
                        AppNavigation(Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}



@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "WYBIERZ AKTYWNOŚĆ",
            fontSize = 54.sp,
            lineHeight = 80.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(80.dp))

        Button(onClick = { navController.navigate("akcelerometr") },modifier = Modifier.fillMaxWidth())
        { Text("AKCELEROMETR", fontSize = 24.sp) }
        Spacer(Modifier.height(32.dp),)


        Button(onClick = { navController.navigate("light") },modifier = Modifier.fillMaxWidth())
        { Text("LUKSOMIERZ", fontSize = 24.sp) }
        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.navigate("zyroskop") },modifier = Modifier.fillMaxWidth())
        { Text("ŻYROSKOP", fontSize = 24.sp) }
        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.navigate("magnetometr") },modifier = Modifier.fillMaxWidth())
        { Text("MAGNETOMETR", fontSize = 24.sp) }
        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.navigate("firestore") },modifier = Modifier.fillMaxWidth())
        { Text("BAZA DANYCH", fontSize = 24.sp) }
    }
}


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start",
        modifier = modifier
    ) {
        composable("start") { StartScreen(navController) } // nowy ekran startowy
        composable("home") { HomeScreen(navController) }
        composable("akcelerometr") { AkcelerometrScreen(navController) }
        composable("zyroskop") { ZyroskopScreen(navController) }
        composable("light") { LightSensorScreen(navController) }
        composable("magnetometr") { MagnetometrScreen(navController) }
        composable("firestore") { DatabaseMenuScreen(navController) }

        composable("accelerometer_db") {
            SensorDatabaseScreen(
                navController = navController,
                collectionName = "accelerometer_data", // zgadza się z tym, gdzie zapisujesz
                valueLabel = "maxG"
            )
        }

        composable("gyroscope_db") {
            SensorDatabaseScreen(
                navController = navController,
                collectionName = "gyroscope_data",
                valueLabel = "maxRotation" // zgadza się z tym, co zapisujesz
            )
        }

        composable("luxometer_db") {
            SensorDatabaseScreen(
                navController = navController,
                collectionName = "light_sensor_data", // pasuje do LightSensorScreen
                valueLabel = "maxLux"                // pasuje do klucza w dokumencie
            )
        }

        composable("magnetometer_db") {
            SensorDatabaseScreen(
                navController = navController,
                collectionName = "magnetometer_data",
                valueLabel = "maxField"
            )
        }


    }
}

@Composable
fun StartScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF81D4FA), Color(0xFFFE9FD8))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "KACPER GŁOWACKI",
                fontSize = 36.sp,
                lineHeight = 80.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "INDEKS:277668",
                fontSize = 36.sp,
                lineHeight = 80.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "WYKORZYSTANIE SENSORÓW",
                fontSize = 36.sp,
                lineHeight = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(100.dp))

            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ZACZYNAMY", fontSize = 40.sp)
            }
        }
    }
}


@Composable
fun DatabaseMenuScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "BAZA DANYCH Z CZUJNIKÓW",
            fontSize = 50.sp,
            lineHeight = 72.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(100.dp))

        Button(
            onClick = { navController.navigate("accelerometer_db") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AKCELEROMETR", fontSize = 24.sp)

        }
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("gyroscope_db") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ŻYROSKOP", fontSize = 24.sp)
        }
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("luxometer_db") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LUKSOMETR", fontSize = 24.sp)
        }
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("magnetometer_db") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("MAGNETOMETR", fontSize = 24.sp)
        }
        Spacer(Modifier.height(100.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("POWRÓT", fontSize = 24.sp)
        }
    }
}



@Composable
fun SensorDatabaseScreen(
    navController: NavHostController,
    collectionName: String,
    valueLabel: String
) {
    val db = remember { FirebaseFirestore.getInstance() }
    var dataList by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var resultMessage by remember { mutableStateOf("") }

    // Listener w czasie rzeczywistym – używamy DisposableEffect
    DisposableEffect(collectionName) {
        val listener = db.collection(collectionName)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    resultMessage = "Błąd: ${error.message}"
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    dataList = snapshot.documents.map { it.data ?: emptyMap() }
                }
            }

        onDispose { listener.remove() } // poprawne wyczyszczenie listenera
    }

    // Funkcja usuwająca wszystkie dokumenty
    fun clearAllData() {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = db.batch()
                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.commit()
                    .addOnSuccessListener { resultMessage = "Wszystkie rekordy usunięte" }
                    .addOnFailureListener { e -> resultMessage = "Błąd przy usuwaniu: ${e.message}" }
            }
            .addOnFailureListener { e ->
                resultMessage = "Błąd przy pobieraniu danych: ${e.message}"
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "ZAPISANE REKORDY",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))

        if (resultMessage.isNotEmpty()) {
            Text(resultMessage, color = Color.Red, fontSize = 18.sp)
            Spacer(Modifier.height(16.dp))
        }

        // Wyświetlanie danych
        for (item in dataList) {
            val value = item[valueLabel] ?: "Brak"
            val timestamp = item["timestamp"]?.let {
                val date = Date(it as Long)
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
            } ?: "Brak daty"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("$valueLabel: $value", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Czas: $timestamp", fontSize = 16.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { clearAllData() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("WYCZYŚĆ WSZYSTKIE", color = Color.White, fontSize = 20.sp)
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("POWRÓT", fontSize = 24.sp)
        }
    }
}



@Composable
fun AkcelerometrScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    // Firestore
    val db = remember { FirebaseFirestore.getInstance() }
    val collectionName = "accelerometer_data"

    // Zmienne
    var currentAcceleration by remember { mutableStateOf(0f) }
    var maxAcceleration by remember { mutableStateOf(0f) }
    var resultMessage by remember { mutableStateOf("") }

    // Listener czujnika
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val netAcceleration = sqrt(x * x + y * y + z * z) / 9.81f

                    currentAcceleration = netAcceleration
                    if (netAcceleration > maxAcceleration) {
                        maxAcceleration = netAcceleration
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Rejestracja czujnika
    DisposableEffect(Unit) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
        onDispose { sensorManager.unregisterListener(sensorEventListener) }
    }

    // Progress bar
    val maxGForBar = 4f
    val progress = (maxAcceleration / maxGForBar).coerceIn(0f, 1f)
    val barColor = when {
        progress < 0.33f -> Color(0xFF00C853)
        progress < 0.66f -> Color(0xFFFFEB3B)
        else -> Color(0xFFFF5252)
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("AKCELEROMETR", fontSize = 40.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))

        // Aktualne przyspieszenie
        Text(
            "AKTUALNE: ${"%.2f".format(currentAcceleration)} G",
            fontSize = 32.sp
        )
        Spacer(Modifier.height(16.dp))

        // Max przyspieszenie
        Text(
            "MAX: ${"%.2f".format(maxAcceleration)} G",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(32.dp))

        // Pasek postępu max G
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(barColor)
            )
        }

        Spacer(Modifier.height(32.dp))

        // Przycisk reset max
        Button(onClick = { maxAcceleration = 0f }) {
            Text("RESET MAX", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Przycisk zapisu max do Firestore
        Button(onClick = {
            val data = hashMapOf(
                "maxG" to maxAcceleration,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection(collectionName)
                .add(data)
                .addOnSuccessListener { resultMessage = "Pomyślnie zapisano" }
                .addOnFailureListener { e -> resultMessage = "Błąd: ${e.message}" }
        }) {
            Text("ZAPISZ MAX DO BAZY", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Komunikat wyniku zapisu
        if (resultMessage.isNotEmpty()) {
            Text(resultMessage, fontSize = 20.sp, color = Color.Blue)
        }

        Spacer(Modifier.height(32.dp))

        // Powrót
        Button(onClick = { navController.popBackStack() }) {
            Text("POWRÓT", fontSize = 24.sp)
        }
    }
}

@Composable
fun ZyroskopScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    // Firestore
    val db = remember { FirebaseFirestore.getInstance() }
    val collectionName = "gyroscope_data"

    // Zmienne
    var currentRotation by remember { mutableStateOf(0f) }
    var maxRotation by remember { mutableStateOf(0f) }
    var resultMessage by remember { mutableStateOf("") }

    // Listener żyroskopu
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val rotation = sqrt(x * x + y * y + z * z)

                    currentRotation = rotation
                    if (rotation > maxRotation) {
                        maxRotation = rotation
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Rejestracja czujnika
    DisposableEffect(Unit) {
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (gyroscope != null) {
            sensorManager.registerListener(
                sensorEventListener,
                gyroscope,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    // Progress bar
    val maxRadForBar = 10f
    val progress = (maxRotation / maxRadForBar).coerceIn(0f, 1f)
    val barColor = when {
        progress < 0.33f -> Color(0xFF00C853)
        progress < 0.66f -> Color(0xFFFFEB3B)
        else -> Color(0xFFFF5252)
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ŻYROSKOP", fontSize = 40.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))

        Text(
            "AKTUALNE: ${"%.2f".format(currentRotation)} rad/s",
            fontSize = 32.sp
        )
        Spacer(Modifier.height(16.dp))

        Text(
            "MAX: ${"%.2f".format(maxRotation)} rad/s",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(barColor)
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(onClick = { maxRotation = 0f }) {
            Text("RESET MAX", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val data = hashMapOf(
                "maxRotation" to maxRotation,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection(collectionName)
                .add(data)
                .addOnSuccessListener {
                    resultMessage = "Pomyślnie zapisano"
                }
                .addOnFailureListener { e ->
                    resultMessage = "Błąd: ${e.message}"
                }
        }) {
            Text("ZAPISZ MAX DO BAZY", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        if (resultMessage.isNotEmpty()) {
            Text(resultMessage, fontSize = 20.sp, color = Color.Blue)
        }

        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("POWRÓT", fontSize = 24.sp)
        }
    }
}


@Composable
fun MagnetometrScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    // Firestore
    val db = remember { FirebaseFirestore.getInstance() }
    val collectionName = "magnetometer_data"

    // Zmienne
    var netField by remember { mutableStateOf(0f) }
    var maxField by remember { mutableStateOf(0f) }
    var resultMessage by remember { mutableStateOf("") }

    // Listener magnetometru
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    netField = sqrt(x * x + y * y + z * z)
                    if (netField > maxField) maxField = netField
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Rejestracja sensora
    DisposableEffect(Unit) {
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (magnetometer != null) {
            sensorManager.registerListener(
                sensorEventListener,
                magnetometer,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        onDispose { sensorManager.unregisterListener(sensorEventListener) }
    }

    // Skalowanie wartości do paska (48–50 µT)
    val minField = 48.6f
    val maxFieldForBar = 48.8f
    val progress = ((netField - minField) / (maxFieldForBar - minField)).coerceIn(0f, 1f)
    val barColor = when {
        progress < 0.33f -> Color(0xFF00C853)
        progress < 0.66f -> Color(0xFFFFEB3B)
        else -> Color(0xFFFF5252)
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MAGNETOMETR", fontSize = 40.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))

        Text("NET: ${"%.2f".format(netField)} µT", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Text("MAX: ${"%.2f".format(maxField)} µT", fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(barColor)
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(onClick = { maxField = 0f }) {
            Text("RESET MAX", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val data = hashMapOf(
                "maxField" to maxField,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection(collectionName)
                .add(data)
                .addOnSuccessListener { resultMessage = "Pomyślnie zapisano" }
                .addOnFailureListener { e -> resultMessage = "Błąd: ${e.message}" }
        }) {
            Text("ZAPISZ MAX DO BAZY", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        if (resultMessage.isNotEmpty()) {
            Text(resultMessage, fontSize = 20.sp, color = Color.Blue)
        }

        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("POWRÓT", fontSize = 24.sp)
        }
    }
}


@Composable
fun LightSensorScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    // Firestore
    val db = remember { FirebaseFirestore.getInstance() }
    val collectionName = "light_sensor_data"

    // Zmienne
    var currentLux by remember { mutableStateOf(0f) }
    var maxLux by remember { mutableStateOf(0f) }
    var resultMessage by remember { mutableStateOf("") }

    // Listener czujnika światła
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                    val lux = event.values[0]
                    currentLux = lux
                    if (lux > maxLux) {
                        maxLux = lux
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Rejestracja czujnika
    DisposableEffect(Unit) {
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(
            sensorEventListener,
            lightSensor,
            SensorManager.SENSOR_DELAY_UI
        )
        onDispose { sensorManager.unregisterListener(sensorEventListener) }
    }

    // Pasek postępu (zakładamy maksymalnie 10000 luksów dla progresu)
    val maxLuxForBar = 10000f
    val progress = (maxLux / maxLuxForBar).coerceIn(0f, 1f)
    val barColor = when {
        progress < 0.33f -> Color(0xFF00C853)
        progress < 0.66f -> Color(0xFFFFEB3B)
        else -> Color(0xFFFF5252)
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Czujnik światła", fontSize = 40.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))

        // Aktualne natężenie światła
        Text("AKTUALNE: ${"%.2f".format(currentLux)} lx", fontSize = 32.sp)
        Spacer(Modifier.height(16.dp))

        // Max natężenie światła
        Text("MAX: ${"%.2f".format(maxLux)} lx", fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))

        // Pasek postępu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(barColor)
            )
        }

        Spacer(Modifier.height(32.dp))

        // Przycisk reset max
        Button(onClick = { maxLux = 0f }) {
            Text("RESET MAX", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Przycisk zapisu max do Firestore
        Button(onClick = {
            val data = hashMapOf(
                "maxLux" to maxLux,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection(collectionName)
                .add(data)
                .addOnSuccessListener { resultMessage = "Pomyślnie zapisano" }
                .addOnFailureListener { e -> resultMessage = "Błąd: ${e.message}" }
        }) {
            Text("ZAPISZ MAX DO BAZY", fontSize = 20.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Komunikat wyniku zapisu
        if (resultMessage.isNotEmpty()) {
            Text(resultMessage, fontSize = 20.sp, color = Color.Blue)
        }

        Spacer(Modifier.height(32.dp))

        // Powrót
        Button(onClick = { navController.popBackStack() }) {
            Text("POWRÓT", fontSize = 24.sp)
        }
    }
}
