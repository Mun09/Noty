package com.mun09.noty

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class ExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        window.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 너비 90%
            (resources.displayMetrics.heightPixels * 0.6).toInt() // 높이 60%
        )

        val editText = findViewById<EditText>(R.id.editText)

        // SharedPreferences에서 기존 텍스트 가져와 표시
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val savedText = prefs.getString("widget_text", "") ?: ""
        editText.setText(savedText)

        // 자동 포커스
        editText.requestFocus()
    }

    override fun onPause() {
        super.onPause()

        val editText = findViewById<EditText>(R.id.editText)
        val text = editText.text.toString()

        // 저장
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        prefs.edit() { putString("widget_text", text) }
        updateWidget(this);

        // 위젯 업데이트
//        val intent = Intent(this, ExampleAppWidgetProvider::class.java).apply {
//            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//        }
//        val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, ExampleAppWidgetProvider::class.java))
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
//        sendBroadcast(intent)

        finish()
    }
}