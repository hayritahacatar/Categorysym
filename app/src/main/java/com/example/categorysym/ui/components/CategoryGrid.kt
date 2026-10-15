package com.example.categorysym.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.categorysym.data.AppItem
import com.example.categorysym.utils.getInstalledApps
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawingPadding
@Composable
fun CategoryGrid() {
    val context = LocalContext.current
    val apps = remember { mutableStateListOf<AppItem>() }

    LaunchedEffect(Unit) {
        apps.addAll(getInstalledApps(context))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxSize()
            .safeDrawingPadding()
    ) {
        items(apps) { app ->
            AppCard(app)
        }
    }
}