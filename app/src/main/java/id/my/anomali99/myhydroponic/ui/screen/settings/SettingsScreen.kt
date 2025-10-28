package id.my.anomali99.myhydroponic.ui.screen.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import id.my.anomali99.myhydroponic.ui.theme.MyHydroponicTheme

// --- Layar 2: SettingsScreen ---
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController) {

    var notificationEnabled by remember { mutableStateOf(true) }
    var thresholdEnabled by remember { mutableStateOf(false) }

    var phMin by remember { mutableStateOf("6.0") }
    var phMax by remember { mutableStateOf("7.0") }
    var tdsMin by remember { mutableStateOf("800") }
    var tdsMax by remember { mutableStateOf("1200") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            NotificationSettingsCard(
                notificationEnabled = notificationEnabled,
                onCheckedChange = { notificationEnabled = it }
            )

            ThresholdSettingsCard(
                thresholdEnabled = thresholdEnabled,
                onCheckedChange = { thresholdEnabled = it },
                phMin = phMin,
                onPhMinChange = { phMin = it },
                phMax = phMax,
                onPhMaxChange = { phMax = it },
                tdsMin = tdsMin,
                onTdsMinChange = { tdsMin = it },
                tdsMax = tdsMax,
                onTdsMaxChange = { tdsMax = it }
            )
        }
    }
}

@Composable
fun NotificationSettingsCard(
    notificationEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Notifikasi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Aktifkan Notifikasi",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = notificationEnabled,
                    onCheckedChange = onCheckedChange
                )
            }
        }
    }
}

@Composable
fun ThresholdSettingsCard(
    thresholdEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    phMin: String, onPhMinChange: (String) -> Unit,
    phMax: String, onPhMaxChange: (String) -> Unit,
    tdsMin: String, onTdsMinChange: (String) -> Unit,
    tdsMax: String, onTdsMaxChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Pengaturan Ambang Batas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Aktifkan Ambang Batas",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = thresholdEnabled,
                    onCheckedChange = onCheckedChange
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Field untuk pH
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = phMin,
                    onValueChange = onPhMinChange,
                    label = { Text("pH Minimal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = thresholdEnabled, // Logika enable/disable
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = phMax,
                    onValueChange = onPhMaxChange,
                    label = { Text("pH Maksimal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = thresholdEnabled, // Logika enable/disable
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Field untuk TDS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = tdsMin,
                    onValueChange = onTdsMinChange,
                    label = { Text("TDS Min (ppm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = thresholdEnabled, // Logika enable/disable
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = tdsMax,
                    onValueChange = onTdsMaxChange,
                    label = { Text("TDS Max (ppm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = thresholdEnabled, // Logika enable/disable
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Tombol Simpan dan Batal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End // Taruh tombol di kanan
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Aksi Batal */ },
                    enabled = thresholdEnabled // Logika enable/disable
                ) {
                    Text("Batal")
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = { /* TODO: Aksi Simpan */ },
                    enabled = thresholdEnabled // Logika enable/disable
                ) {
                    Text("Simpan")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    MyHydroponicTheme {
        SettingsScreen(rememberNavController())
    }
}