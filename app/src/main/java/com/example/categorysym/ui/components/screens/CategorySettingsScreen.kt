package com.example.categorysym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.categorysym.data.CustomCategory
import com.example.categorysym.data.LauncherDataStore
import com.example.categorysym.ui.components.dialogs.CategoryUpsertDialog
import com.example.categorysym.utils.CategoryRegistry
import com.example.categorysym.utils.parseColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySettingsScreen(
    enabledCategories: Set<String>,
    customCategories: List<CustomCategory>,
    purchasedSlots: Int,
    onToggleCategory: (String) -> Unit,
    onPurchaseSlot: () -> Unit,
    onAddCustomCategory: (String, String, String, String) -> Unit,
    onUpdateCustomCategory: (String, String, String, String, String) -> Unit,
    onDeleteCustomCategory: (String) -> Unit,
    onResetManualCategories: () -> Unit,
    onDismiss: () -> Unit
) {
    var showAboutDialog by remember { mutableStateOf(false) }
    var infoCategoryName by remember { mutableStateOf<String?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CustomCategory?>(null) }
    var deleteConfirmId by remember { mutableStateOf<String?>(null) }
    var showResetConfirm by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Manage Categories",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 450.dp)) {
                // 1. Standard Categories
                item {
                    Text("Standard System", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                val allStandard = LauncherDataStore.DEFAULT_CATEGORIES + LauncherDataStore.OPTIONAL_CATEGORIES
                
                items(allStandard.toList()) { category ->
                    val isOptional = LauncherDataStore.OPTIONAL_CATEGORIES.contains(category)
                    val isEnabled = enabledCategories.contains(category)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { infoCategoryName = category }) {
                            Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Gray, modifier = Modifier.size(20.dp))
                        }
                        
                        Text(text = category, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        
                        if (isOptional) {
                            Switch(
                                checked = isEnabled,
                                onCheckedChange = { onToggleCategory(category) }
                            )
                        } else {
                            Text("Always On", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(end = 12.dp))
                        }
                    }
                }

                // 2. Premium Section
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Premium Slots", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                item {
                    val slotsAvailable = purchasedSlots - customCategories.size
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Purchased Slots: $purchasedSlots/10",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Available to create: $slotsAvailable",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                            Button(
                                onClick = onPurchaseSlot,
                                enabled = purchasedSlots < 10,
                                modifier = Modifier.weight(1f).padding(end = 4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Buy Slot ($0.99)")
                            }
                            
                            Button(
                                onClick = { showCreateDialog = true },
                                enabled = slotsAvailable > 0,
                                modifier = Modifier.weight(1f).padding(start = 4.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Create")
                            }
                        }
                    }
                }

                items(customCategories) { custom ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { infoCategoryName = custom.name }) {
                            Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Gray, modifier = Modifier.size(20.dp))
                        }
                        
                        Box(modifier = Modifier.size(12.dp).background(parseColor(custom.colorHex), CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(text = custom.name, fontSize = 16.sp, modifier = Modifier.weight(1f))

                        IconButton(onClick = { editingCategory = custom }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(20.dp))
                        }
                        
                        IconButton(onClick = { deleteConfirmId = custom.id }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showResetConfirm = true }
            ) {
                Text(
                    text = "Reset App Categorization",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = Color.Red.copy(alpha = 0.7f)
                )
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showAboutDialog = true }
            ) {
                Text(
                    text = "About Categorysym",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = Color.Black
                )
            }
        }
    }

    // Info Dialog
    infoCategoryName?.let { name ->
        val info = CategoryRegistry.infoMap[name]
        val custom = customCategories.find { it.name == name }
        
        AlertDialog(
            onDismissRequest = { infoCategoryName = null },
            confirmButton = {
                TextButton(onClick = { infoCategoryName = null }) {
                    Text("Got it")
                }
            },
            title = { Text(name) },
            text = {
                Column {
                    if (info != null) {
                        Text(text = info.description, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Examples:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Text(text = info.examples, fontSize = 14.sp)
                    } else if (custom != null) {
                        Text(text = custom.note, fontSize = 14.sp)
                        if (custom.examples.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Examples:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Text(text = custom.examples, fontSize = 14.sp)
                        }
                    }
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text("About Categorysym") },
            text = {
                Text(
                    text = "\"A mobile design philosophy inspired by the ancestors of this industry, prioritizing utility over decoration. It neither hides complexity behind animations nor overhauls unambiguous functions. By treating your most personal device as an extension of self, it harmonizes everything you do with the needs of the future.\"",
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        )
    }

    if (showCreateDialog) {
        CategoryUpsertDialog(
            onConfirm = { name, color, note, examples ->
                onAddCustomCategory(name, color, note, examples)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    if (editingCategory != null) {
        CategoryUpsertDialog(
            initialCategory = editingCategory,
            onConfirm = { name, color, note, examples ->
                editingCategory?.id?.let { id ->
                    onUpdateCustomCategory(id, name, color, note, examples)
                }
                editingCategory = null
            },
            onDismiss = { editingCategory = null }
        )
    }

    if (deleteConfirmId != null) {
        val categoryToDelete = customCategories.find { it.id == deleteConfirmId }
        AlertDialog(
            onDismissRequest = { deleteConfirmId = null },
            title = { Text("Delete Custom Category?") },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to delete \"${categoryToDelete?.name}\"?",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Apps inside this category will not be deleted from your phone. " +
                                "They will be moved to 'Uncategorized' or return to their original category based on the system's logic.",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        deleteConfirmId?.let { onDeleteCustomCategory(it) }
                        deleteConfirmId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Confirm Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmId = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Reset All Manual Moves?") },
            text = {
                Text("This will clear all manual app assignments. Every application will return to its default system-assigned category. This action is permanent.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetManualCategories()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reset Everything", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
