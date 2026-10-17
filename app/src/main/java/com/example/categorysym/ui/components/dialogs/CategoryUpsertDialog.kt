package com.example.categorysym.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.categorysym.R
import com.example.categorysym.data.CustomCategory

@Composable
fun CategoryUpsertDialog(
    initialCategory: CustomCategory? = null,
    onConfirm: (name: String, color: String, note: String, examples: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(initialCategory?.name ?: "") }
    var colorHex by remember { mutableStateOf(initialCategory?.colorHex ?: "#616161") }
    var note by remember { mutableStateOf(initialCategory?.note ?: "") }
    var examples by remember { mutableStateOf(initialCategory?.examples ?: "") }
    
    val isEditing = initialCategory != null
    
    val presetColors = listOf(
        "#F44336", "#E91E63", "#9C27B0", "#673AB7", 
        "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4", 
        "#009688", "#4CAF50", "#8BC34A", "#CDDC39", 
        "#FFEB3B", "#FFC107", "#FF9800", "#FF5722"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) stringResource(R.string.edit_custom_category) else stringResource(R.string.create_custom_category)) },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name, colorHex, note, examples) },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(if (isEditing) stringResource(R.string.save) else stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.category_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(stringResource(R.string.select_color), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                
                // Color Grid
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 4
                ) {
                    presetColors.forEach { hex ->
                        val isSelected = colorHex.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .background(Color(android.graphics.Color.parseColor(hex)), CircleShape)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) Color.Black else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { colorHex = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = colorHex,
                    onValueChange = { if (it.startsWith("#") && (it.length <= 7)) colorHex = it },
                    label = { Text(stringResource(R.string.custom_hex_color)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("#RRGGBB") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.description_manifesto)) },
                    placeholder = { Text(stringResource(R.string.description_placeholder)) },
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = examples,
                    onValueChange = { examples = it },
                    label = { Text(stringResource(R.string.example_apps)) },
                    placeholder = { Text(stringResource(R.string.example_apps_placeholder)) },
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                )
            }
        }
    )
}

@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        maxItemsInEachRow = maxItemsInEachRow
    ) {
        content()
    }
}