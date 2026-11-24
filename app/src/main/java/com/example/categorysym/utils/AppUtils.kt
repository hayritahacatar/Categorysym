package com.example.categorysym.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.categorysym.data.AppItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun getInstalledApps(context: Context): List<AppItem> {
    val pm = context.packageManager

    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val apps = try {
        pm.queryIntentActivities(
            intent,
            PackageManager.MATCH_ALL,
        )
    } catch (_: Exception) {
        emptyList()
    }

    return apps.mapNotNull { resolveInfo ->
        try {
            val ai = resolveInfo.activityInfo
            if (ai != null && ai.packageName != null) {
                val label = resolveInfo.loadLabel(pm).toString()
                val icon = resolveInfo.loadIcon(pm)
                AppItem(
                    name = label,
                    packageName = ai.packageName,
                    icon = icon
                )
            } else null
        } catch (_: Exception) {
            null
        }
    }
}

fun getAppsFlow(context: Context): Flow<List<AppItem>> = callbackFlow {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            trySend(getInstalledApps(context))
        }
    }

    val filter = IntentFilter().apply {
        addAction(Intent.ACTION_PACKAGE_ADDED)
        addAction(Intent.ACTION_PACKAGE_REMOVED)
        addAction(Intent.ACTION_PACKAGE_REPLACED)
        addDataScheme("package")
    }

    ContextCompat.registerReceiver(
        context,
        receiver,
        filter,
        ContextCompat.RECEIVER_EXPORTED
    )

    // Initial emission
    trySend(getInstalledApps(context))

    awaitClose {
        try {
            context.unregisterReceiver(receiver)
        } catch (_: Exception) {}
    }
}