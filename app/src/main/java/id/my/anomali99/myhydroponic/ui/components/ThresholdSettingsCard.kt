package id.my.anomali99.myhydroponic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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