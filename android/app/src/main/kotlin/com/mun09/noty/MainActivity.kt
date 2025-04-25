package com.mun09.noty

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    companion object {
        private const val PREF_NAME = "MyAppPrefs"
        private const val PREF_FIRST_RUN = "isFirstRun"
        private const val CHANNEL_CODE = "com.example/native"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 첫 실행 여부 확인 및 처리
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean(PREF_FIRST_RUN, true)

        if (isFirstRun) {
            // 첫 실행 시 실행할 코드
            runFirstTimeSetup()

            // 첫 실행 플래그를 false로 설정
            with(prefs.edit()) {
                putBoolean(PREF_FIRST_RUN, false)
                apply()
            }
        }
    }

    private fun runFirstTimeSetup() {
        // 첫 실행 시 실행할 코드
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("widget_color", 0xFFFFFFFF.toInt()) // 기본 색상 (예: 투명 또는 기본값)
            putFloat("widget_fontSize", 16f) // 기본 폰트 크기
            apply()
        }
        Log.d("MainActivity", "First run setup completed")
    }

//    private val CHANNEL_CODE = "com.example/native"

    // configureFlutterEngine에서 MethodChannel 설정
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Flutter와의 통신을 설정하기 위해 MethodChannel을 사용
        MethodChannel(
            flutterEngine.dartExecutor,
            CHANNEL_CODE
        ).setMethodCallHandler { call, result ->
            if (call.method == "saveText") {
                val text = call.argument<String>("text")
                if (text != null) {
                    saveTextToSharedPreferences(text)
                    updateWidget(this)
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
            } else if (call.method == "saveSetting") {
                val color = (call.argument<Number>("color") ?: 0).toInt()
                val fontSize = (call.argument<Number>("fontSize") ?: 16.0).toDouble()
                saveSettingToSharedPreferences(color, fontSize)
                updateWidget(this)
                result.success("Text saved successfully")
            } else if (call.method == "loadSetting") {
                val savedSetting = loadSettingFromSharedPreferences()
                if (savedSetting["color"] != -1 && savedSetting["fontSize"] != -1f) {
                    result.success(savedSetting)
                } else {
                    result.error("UNAVAILABLE", "Some properties of setting is null", null)
                }
            } else {
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

    private fun saveSettingToSharedPreferences(color: Int, fontSize: Double) {
        val prefs: SharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("widget_color", color)
            putFloat("widget_fontSize", fontSize.toFloat())
            apply()
        }
    }

    private fun loadSettingFromSharedPreferences(): Map<String, Any> {
        val prefs: SharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val color = prefs.getInt("widget_color", -1)
        val fontSize = prefs.getFloat("widget_fontSize", -1f)
        return mapOf("color" to color, "fontSize" to fontSize.toDouble())
    }

}
