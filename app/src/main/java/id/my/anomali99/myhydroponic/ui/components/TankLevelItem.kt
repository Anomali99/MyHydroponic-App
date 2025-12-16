package id.my.anomali99.myhydroponic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun TankLevelItem(name: String, level: Float, max: Float, min: Float = 0f, modifier: Modifier = Modifier) {
    val currentLevel = level.coerceIn(0f, max)
    val progressValue = (currentLevel / max).coerceIn(0f, 1f)
    val percentage = (progressValue * 100).toInt()

    val progressColor = when {
        level < min -> MaterialTheme.colorScheme.error
        progressValue < 0.5f -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    fun formatValue(value: Float): String {
        val localeID = Locale("id", "ID")
        return String.format(localeID, "%.2f", value)
            .trimEnd('0')
            .trimEnd(',')
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(
                text = "${formatValue(currentLevel)} / ${formatValue(max)} ml (${percentage}%)",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = progressColor
            )
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = progressValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    }
}