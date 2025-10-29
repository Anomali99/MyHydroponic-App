package id.my.anomali99.myhydroponic.ui.screen.settings

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.core.content.ContextCompat

import id.my.anomali99.myhydroponic.ui.theme.MyHydroponicTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                viewModel.onNotificationEnabledChanged(true)
            }
        }
    )

    val smartOnCheckedChange = { newCheckedState: Boolean ->
        if (newCheckedState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permission = Manifest.permission.POST_NOTIFICATIONS
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    viewModel.onNotificationEnabledChanged(true)
                } else {
                    permissionLauncher.launch(permission)
                }
            } else {
                viewModel.onNotificationEnabledChanged(true)
            }
        } else {
            viewModel.onNotificationEnabledChanged(false)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissError()
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar(
                message = "Pengaturan berhasil disimpan!",
                duration = SnackbarDuration.Short
            )
            viewModel.dismissSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                NotificationSettingsCard(
                    notificationEnabled = uiState.notificationEnabled,
                    onCheckedChange = smartOnCheckedChange,
                    enabled = !uiState.isLoading
                )

                ThresholdSettingsCard(
                    thresholdEnabled = uiState.thresholdEnabled,
                    onCheckedChange = viewModel::onThresholdEnabledChanged,
                    phMin = uiState.phMin,
                    onPhMinChange = viewModel::onPhMinChanged,
                    phMax = uiState.phMax,
                    onPhMaxChange = viewModel::onPhMaxChanged,
                    tdsMin = uiState.tdsMin,
                    onTdsMinChange = viewModel::onTdsMinChanged,
                    tdsMax = uiState.tdsMax,
                    onTdsMaxChange = viewModel::onTdsMaxChanged,
                    onSaveClick = viewModel::onSaveClicked,
                    onCancelClick = viewModel::onCancelClicked,
                    enabled = !uiState.isLoading
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun NotificationSettingsCard(
    notificationEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean
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
                    onCheckedChange = onCheckedChange,
                    enabled = enabled
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
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    enabled: Boolean
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
                    onCheckedChange = onCheckedChange,
                    enabled = enabled
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            val fieldsEnabled = enabled && thresholdEnabled

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = phMin,
                    onValueChange = onPhMinChange,
                    label = { Text("pH Minimal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = fieldsEnabled,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = phMax,
                    onValueChange = onPhMaxChange,
                    label = { Text("pH Maksimal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = fieldsEnabled,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = tdsMin,
                    onValueChange = onTdsMinChange,
                    label = { Text("TDS Min (ppm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = fieldsEnabled,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = tdsMax,
                    onValueChange = onTdsMaxChange,
                    label = { Text("TDS Max (ppm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = fieldsEnabled,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onCancelClick
                ) {
                    Text("Batal")
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = onSaveClick
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