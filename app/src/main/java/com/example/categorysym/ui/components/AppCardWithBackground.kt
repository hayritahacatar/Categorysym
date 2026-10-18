package com.example.categorysym.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.categorysym.R
import com.example.categorysym.data.AppItem
import com.example.categorysym.utils.getCategoryDisplayName

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun AppCardWithBackground(
    app: AppItem,
    color: Color,
    categoryNames: List<String>,
    isMoveEnabled: Boolean = true,
    onAppClick: (AppItem) -> Unit,
    onCategoryMove: (String) -> Unit,
    onToggleHide: () -> Unit = {},
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var showSheet by remember { mutableStateOf(value = false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.combinedClickable(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onAppClick(app)

                val intent = context.packageManager
                    .getLaunchIntentForPackage(app.packageName)

                intent?.let { context.startActivity(it) }
            },
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                showSheet = true
            }
        )
    ) {
        AsyncImage(
            model = app.icon,
            contentDescription = app.name,
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = app.name,
            fontSize = 7.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isMoveEnabled) stringResource(R.string.move_app_to, app.name) else stringResource(R.string.hidden_app_options),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onToggleHide()
                        showSheet = false
                    }
                ) {
                    Text(
                        text = if (isMoveEnabled) stringResource(R.string.hide_app) else stringResource(R.string.unhide_app),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (isMoveEnabled) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 380.dp)
                    ) {
                        items(categoryNames) { category ->
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onCategoryMove(category)
                                    showSheet = false
                                }
                            ) {
                                Text(
                                    text = getCategoryDisplayName(category),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        try {
                            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", app.packageName, null)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.util.Log.e("AppCard", "Could not open app settings for ${app.packageName}", e)
                        }
                        showSheet = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.app_info),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        try {
                            val uninstallIntent = Intent(Intent.ACTION_DELETE, Uri.fromParts("package", app.packageName, null)).apply {
                                putExtra(Intent.EXTRA_RETURN_RESULT, true)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(uninstallIntent)
                        } catch (_: Exception) {
                            try {
                                @Suppress("DEPRECATION")
                                val fallbackIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:${app.packageName}")).apply {
                                    putExtra(Intent.EXTRA_RETURN_RESULT, true)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(fallbackIntent)
                            } catch (ex: Exception) {
                                android.util.Log.e("AppCard", "Could not launch uninstall intent for ${app.packageName}", ex)
                                android.widget.Toast.makeText(context, context.getString(R.string.could_not_uninstall, app.name), android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        showSheet = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.uninstall_app),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}