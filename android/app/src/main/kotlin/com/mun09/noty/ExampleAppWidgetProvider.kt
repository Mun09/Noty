package com.mun09.noty

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class ExampleAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val savedText = prefs.getString("widget_text", "기본 텍스트") // 저장된 텍스트 or 기본값
        val savedTextColor = prefs.getInt("widget_color", 0) // 저장된 텍스트 or 기본값")
        val savedTextFontSize = prefs.getFloat("widget_fontSize", 16f) // 저장된 텍스트 or 기본값")

        appWidgetIds.forEach { appWidgetId ->
            val intent = Intent(context, ExampleActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val pendingIntent = PendingIntent.getActivity(
                context, appWidgetId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(context.packageName, R.layout.appwidget_provider_layout)
            views.setTextViewText(R.id.widget_text, savedText)
            views.setTextColor(R.id.widget_text, savedTextColor)
            views.setFloat(R.id.widget_text, "setTextSize", savedTextFontSize)

            views.setOnClickPendingIntent(R.id.layoutId, pendingIntent)
            views.setOnClickPendingIntent(R.id.widget_text, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}