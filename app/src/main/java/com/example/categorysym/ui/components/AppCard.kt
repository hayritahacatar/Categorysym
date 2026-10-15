package com.example.categorysym.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.categorysym.data.AppItem
import androidx.compose.ui.Alignment

@Composable
fun AppCard(app: AppItem) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                val intent = context.packageManager
                    .getLaunchIntentForPackage(app.packageName)

                intent?.let { context.startActivity(it) }
            }
    ) {
        AsyncImage(
            model = app.icon,
            contentDescription = app.name
        )

        Text(
            text = app.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}