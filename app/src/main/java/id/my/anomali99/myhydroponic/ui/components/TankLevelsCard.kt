package id.my.anomali99.myhydroponic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TankLevelsCard(
    mainLevel: Float,
    aLevel: Float,
    bLevel: Float,
    phUpLevel: Float,
    phDownLevel: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Level Tangki Cairan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Divider(Modifier.padding(vertical = 4.dp))

            TankLevelItem(name = "Tangki Utama", level = mainLevel, max = 22400f, min=10028.5f)
            TankLevelItem(name = "Nutrisi A", level = aLevel, max = 377.14f, min=112.5f)
            TankLevelItem(name = "Nutrisi B", level = bLevel, max = 377.14f, min=112.5f)
            TankLevelItem(name = "pH-Up", level = phUpLevel, max = 377.14f, min=112.5f)
            TankLevelItem(name = "pH-Down", level = phDownLevel, max = 377.14f, min=112.5f)
        }
    }
}