package com.example.categorysym.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.example.categorysym.data.AppItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun getInstalledApps(context: Context): List<AppItem> {
    val pm = context.packageManager

    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val apps = pm.queryIntentActivities(
        intent,
        PackageManager.MATCH_ALL
    )

    return apps.map {
        val ai = it.activityInfo

        AppItem(
            name = it.loadLabel(pm).toString(),
            packageName = ai.packageName,
            icon = it.loadIcon(pm)
        )
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

    context.registerReceiver(receiver, filter)

    // Initial emission
    trySend(getInstalledApps(context))

    awaitClose {
        context.unregisterReceiver(receiver)
    }
}
