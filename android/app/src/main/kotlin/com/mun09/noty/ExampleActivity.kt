package com.mun09.noty

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.activity.OnBackPressedCallback

class ExampleActivity : AppCompatActivity() {
    private var isKeyboardVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        window.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(), // 너비 90%
            (resources.displayMetrics.heightPixels * 0.6).toInt() // 높이 60%
        )

        val editText = findViewById<EditText>(R.id.editText)

        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            val heightDiff = screenHeight - r.height()

            Log.d("ExampleActivity", "heightDiff: $heightDiff")

            isKeyboardVisible = heightDiff > screenHeight * 0.15
        }

        // SharedPreferences에서 기존 텍스트 가져와 표시
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val savedText = prefs.getString("widget_text", "") ?: ""
        val savedTextColor = prefs.getInt("widget_color", 0xFFFFFFFF.toInt())
        val savedTextFontSize = prefs.getFloat("widget_fontSize", 16f)
        editText.setText(savedText)
        editText.setTextColor(savedTextColor)
        editText.setTextSize(savedTextFontSize)

        // 자동 포커스
        editText.requestFocus()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                if (isKeyboardVisible) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        onPause()
                    }, 300)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        onPause()
                    }, 100)
                }

//                if (isKeyboardVisible) {
//                    // 키보드가 열려 있으면 닫기
//                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(editText.windowToken, 0)
//                    Handler(Looper.getMainLooper()).postDelayed({
//                    }, 100)
//                    onPause()
//                } else {
//                    // 키보드가 닫혀 있으면 종료
//                    Handler(Looper.getMainLooper()).postDelayed({
//                    }, 500)
//                    onPause()
//                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val editText = findViewById<EditText>(R.id.editText)
        val text = editText.text.toString()

        // 저장
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        prefs.edit() { putString("widget_text", text) }
        updateWidget(this)
        finish()
    }
}