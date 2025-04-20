package com.mun09.noty

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

fun updateWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val widgetComponent = ComponentName(context, ExampleAppWidgetProvider::class.java)
    val appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)

    val intent = Intent(context, ExampleAppWidgetProvider::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    }

    context.sendBroadcast(intent)
}
