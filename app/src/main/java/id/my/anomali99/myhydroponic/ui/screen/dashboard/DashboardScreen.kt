package id.my.anomali99.myhydroponic.ui.screen.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import id.my.anomali99.myhydroponic.ui.components.ActionButtonCard
import id.my.anomali99.myhydroponic.ui.components.SensorCard
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

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MyHydroponic",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
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
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Data diambil:\n${uiState.datetime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = viewModel::onRefreshClicked,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Refresh", modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Refresh")
                        }
                    }

                    Divider()
                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SensorCard(
                            title = "pH",
                            value = uiState.ph,
                            modifier = Modifier.weight(1f)
                        )
                        SensorCard(
                            title = "TDS",
                            value = uiState.tds,
                            unit = "ppm",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    TankLevelsCard(
                        aLevel = uiState.aTank,
                        maxALevel = 20f,
                        bLevel = uiState.bTank,
                        maxBLevel = 20f,
                        phUpLevel = uiState.phUpTank,
                        maxPhUpLevel = 20f,
                        phDownLevel = uiState.phDownTank,
                        maxPhDownLevel = 20f,
                        mainLevel = uiState.mainTank,
                        maxMainLevel = 20f
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Kontrol Manual",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionButtonCard(
                            text = "Tambah\nNutrisi",
                            onClick = viewModel::onAddNutritionClicked,
                            modifier = Modifier.weight(1f)
                        )
                        ActionButtonCard(
                            text = "Tambah\npH-Up",
                            onClick = viewModel::onAddPhUpClicked,
                            modifier = Modifier.weight(1f)
                        )
                        ActionButtonCard(
                            text = "Tambah\npH-Down",
                            onClick = viewModel::onAddPhDownClicked,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
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
