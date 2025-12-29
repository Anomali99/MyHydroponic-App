package id.my.anomali99.myhydroponic.ui.screen.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import id.my.anomali99.myhydroponic.ui.components.ActionButtonCard
import id.my.anomali99.myhydroponic.ui.components.SensorCard
import id.my.anomali99.myhydroponic.ui.components.StatusHeader
import id.my.anomali99.myhydroponic.ui.components.TankLevelsCard
import id.my.anomali99.myhydroponic.ui.navigation.Screen
import id.my.anomali99.myhydroponic.ui.theme.MyHydroponicTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val pullRefreshState = rememberPullToRefreshState()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadSettings()
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

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.reloadDashboard()
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
                        "MyHydroponic",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Pengaturan")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                StatusHeader(
                    isOnline = uiState.deviceIsActive,
                    datetime = uiState.datetime,
                    onRefresh = viewModel::onRefreshClicked
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SensorCard(
                            title = "pH Air",
                            value = uiState.ph,
                            unit = null,
                            modifier = Modifier.weight(1f)
                        )
                        SensorCard(
                            title = "Suhu Air",
                            value = uiState.temp,
                            unit = "°C",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    SensorCard(
                        title = "TDS",
                        value = uiState.tds,
                        unit = "ppm",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                TankLevelsCard(
                    mainLevel = uiState.mainTank,
                    aLevel = uiState.aTank,
                    bLevel = uiState.bTank,
                    phUpLevel = uiState.phUpTank,
                    phDownLevel = uiState.phDownTank
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Kontrol Manual",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionButtonCard(
                            text = "Tambah\nNutrisi",
                            onClick = viewModel::onAddNutritionClicked,
                            modifier = Modifier.weight(1f),
                            enabled = uiState.deviceIsActive
                        )
                        ActionButtonCard(
                            text = "Tambah\npH-Up",
                            onClick = viewModel::onAddPhUpClicked,
                            modifier = Modifier.weight(1f),
                            enabled = uiState.deviceIsActive
                        )
                        ActionButtonCard(
                            text = "Tambah\npH-Down",
                            onClick = viewModel::onAddPhDownClicked,
                            modifier = Modifier.weight(1f),
                            enabled = uiState.deviceIsActive
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
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

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun DashboardScreenPreview() {
    MyHydroponicTheme {
        DashboardScreen(navController = rememberNavController())
    }
}
