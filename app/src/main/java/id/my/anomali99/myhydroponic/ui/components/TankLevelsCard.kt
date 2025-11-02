package id.my.anomali99.myhydroponic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TankLevelsCard(
    mainLevel: Float, maxMainLevel: Float,
    aLevel: Float, maxALevel: Float,
    bLevel: Float, maxBLevel: Float,
    phUpLevel: Float, maxPhUpLevel: Float,
    phDownLevel: Float, maxPhDownLevel: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Level Tangki",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TankLevelItem(name = "Nutrisi A", level = aLevel, max = maxALevel)
            TankLevelItem(name = "Nutrisi B", level = bLevel, max = maxBLevel)
            TankLevelItem(name = "pH-Up", level = phUpLevel, max = maxPhUpLevel)
            TankLevelItem(name = "pH-Down", level = phDownLevel, max = maxPhDownLevel)
            TankLevelItem(name = "Tangki Utama", level = mainLevel, max = maxMainLevel)
        }
    }
}