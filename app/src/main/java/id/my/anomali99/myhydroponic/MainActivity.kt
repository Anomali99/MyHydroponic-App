package id.my.anomali99.myhydroponic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import id.my.anomali99.myhydroponic.ui.navigation.AppNavigation
import id.my.anomali99.myhydroponic.ui.theme.MyHydroponicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MyHydroponicTheme {
                AppNavigation()
            }
        }
    }
}