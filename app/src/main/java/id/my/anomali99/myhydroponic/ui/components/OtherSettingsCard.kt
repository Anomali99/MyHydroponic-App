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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OtherSettingsCard(
    duration: String, onDurationChange: (String) -> Unit,
    maxMain: String, onMaxMainChange: (String) -> Unit,
    maxNutrientA: String, onMaxNutrientAChange: (String) -> Unit,
    maxNutrientB: String, onMaxNutrientBChange: (String) -> Unit,
    maxPhUp: String, onMaxPhUpChange: (String) -> Unit,
    maxPhDown: String, onMaxPhDownChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
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
                text = "Pengaturan Lainya",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            OutlinedTextField(
                value = duration,
                onValueChange = onDurationChange,
                label = { Text("Durasi Aktivasi Pompa (s)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = maxMain,
                onValueChange = onMaxMainChange,
                label = { Text("Level Maksimal Tangki Utama (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = maxNutrientA,
                onValueChange = onMaxNutrientAChange,
                label = { Text("Level Maksimal Tangki Nutrisi A (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = maxNutrientB,
                onValueChange = onMaxNutrientBChange,
                label = { Text("Level Maksimal Tangki Nutrisi B (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = maxPhUp,
                onValueChange = onMaxPhUpChange,
                label = { Text("Level Maksimal Tangki pH-Up (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = maxPhDown,
                onValueChange = onMaxPhDownChange,
                label = { Text("Level Maksimal Tangki pH-Down (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

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