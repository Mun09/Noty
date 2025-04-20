package com.mun09.noty

import android.content.Context
import android.content.SharedPreferences
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val channel_code = "com.example/native"



//    private fun updateWidget() {
//        print("update!!!!");
//        val intent = Intent(this, ExampleAppWidgetProvider::class.java)
//        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//
//        val ids = AppWidgetManager.getInstance(application)
//            .getAppWidgetIds(ComponentName(application, ExampleAppWidgetProvider::class.java))
//
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
//        sendBroadcast(intent)
//    }

    // configureFlutterEngine에서 MethodChannel 설정
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Flutter와의 통신을 설정하기 위해 MethodChannel을 사용
        MethodChannel(flutterEngine.dartExecutor, channel_code).setMethodCallHandler { call, result ->
            if (call.method == "saveText") {
                val text = call.argument<String>("text")
                if (text != null) {
                    saveTextToSharedPreferences(text)
                    updateWidget(this);
                    result.success("Text saved successfully")
                } else {
                    result.error("UNAVAILABLE", "Text is null", null)
                }
            } else if (call.method == "loadText") {
                val savedText = loadTextFromSharedPreferences()
                if (savedText != null) {
                    result.success(savedText)  // 저장된 텍스트 반환
                } else {
                    result.success("")  // 저장된 텍스트가 없다면 빈 문자열 반환
                }
            }else {
                result.notImplemented()
            }
        }
    }

    private fun loadTextFromSharedPreferences(): String? {
        val prefs: SharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return prefs.getString("widget_text", null) // 저장된 텍스트 가져오기
    }

    // SharedPreferences에 텍스트 저장
    private fun saveTextToSharedPreferences(text: String) {
        val prefs: SharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("widget_text", text) // 위젯에 표시할 텍스트 저장
            apply()
        }
    }
}
