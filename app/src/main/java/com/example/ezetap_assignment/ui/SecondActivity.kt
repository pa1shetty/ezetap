package com.example.ezetap_assignment.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ezetap_assignment.databinding.ActivitySecondBinding
import com.example.network.network.response.UiResponse
import com.google.gson.Gson

lateinit var binding: ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getStringExtra("data")
        val uiResponse = Gson().fromJson(data, UiResponse::class.java)
        uiResponse.uiData?.forEach { uiElement ->
            when (uiElement.uiType) {
                "label" -> {
                    val textView = TextView(this)
                    binding.llData.addView(textView)
                    textView.textSize =20f
                    textView.text = uiElement.value
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 0)
                    }
                    textView.layoutParams = param

                }
                "edittext" -> {
                    val textInputEditText =
                        TextView(this)
                    binding.llData.addView(textInputEditText)
                    textInputEditText.textSize =18f
                    textInputEditText.text = uiElement.value
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 30)
                    }
                    textInputEditText.layoutParams = param
                }
                else -> {
                }
            }
        }
    }
}