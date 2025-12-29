package id.my.anomali99.myhydroponic.ui.screen.settings

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.core.content.ContextCompat

import id.my.anomali99.myhydroponic.ui.components.NotificationSettingsCard
import id.my.anomali99.myhydroponic.ui.components.OtherSettingsCard
import id.my.anomali99.myhydroponic.ui.components.ThresholdSettingsCard
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

    val pullRefreshState = rememberPullToRefreshState()

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

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.loadSettings()
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            pullRefreshState.endRefresh()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pengaturan",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
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

                OtherSettingsCard(
                    duration = uiState.duration,
                    token = uiState.apiToken,
                    onDurationChange = viewModel::onDurationChanged,
                    onTokenChange = viewModel::onTokenChanged,
                    onSaveClick = viewModel::onSaveOtherClicked,
                    onCancelClick = viewModel::onCancelOtherClicked,
                    enabled = !uiState.isLoading
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            PullToRefreshContainer(
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
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