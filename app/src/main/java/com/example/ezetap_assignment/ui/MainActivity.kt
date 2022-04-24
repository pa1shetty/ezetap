package com.example.ezetap_assignment.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ezetap_assignment.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUiData()
        observeData()
        setUpSwipe()
    }

    private fun setUpSwipe() {
        binding.srData.setOnRefreshListener {
            mainActivityViewModel.getResponseUI()
        }
    }

    private fun getUiData() {

    }

    private fun observeData() {
        mainActivityViewModel.bitmapStore.observe(this) {
            binding.ivLogo.setImageBitmap(it)
        }
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainActivityViewModel.uiResponse.collectLatest { uiResponse ->
                        // uiResponse.logo_url?.let { mainActivityViewModel.getImage(it) }
                        binding.tvHeading.text = uiResponse.heading_text
                        binding.llData.removeAllViews()

                        uiResponse.uiData?.forEachIndexed { index, uiElement ->
                            when (uiElement.uiType) {
                                "label" -> {
                                    val textView = TextView(this@MainActivity)
                                    textView.text = uiElement.value
                                    binding.llData.addView(textView)
                                }
                                "edittext" -> {
                                    val textInputLayout = TextInputLayout(
                                        this@MainActivity
                                    )
                                    textInputLayout.boxBackgroundMode =
                                        TextInputLayout.BOX_BACKGROUND_OUTLINE
                                    val param = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    ).apply {
                                        setMargins(0, 0, 0, 30)
                                    }
                                    textInputLayout.layoutParams = param

                                    val editText = TextInputEditText(textInputLayout.context)
                                    textInputLayout.addView(
                                        editText,
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        )
                                    )
                                    binding.llData.addView(textInputLayout)
                                    editText.isSingleLine = true
                                    if (uiElement.key == "text_phone") {
                                        editText.inputType = InputType.TYPE_CLASS_PHONE
                                        editText.filters = arrayOf<InputFilter>(LengthFilter(10))
                                    }
                                    if (editText.text.toString() != uiElement.value && uiElement.value != null) {
                                        editText.setText(uiElement.value.toString())
                                    }
                                    editText.addTextChangedListener {
                                        mainActivityViewModel.updateValue(index, it.toString())
                                    }
                                }

                                "button" -> {
                                    val button =
                                        com.google.android.material.button.MaterialButton(this@MainActivity)
                                    button.text = uiElement.value
                                    binding.llData.addView(button)
                                    button.setOnClickListener {
                                        var allDataInserted = true
                                        uiResponse.uiData?.forEach { uiElement ->
                                            if (uiElement.uiType == "edittext" && uiElement.value == null) {
                                                allDataInserted = false
                                                return@forEach
                                            }
                                        }
                                        if (allDataInserted) {
                                            startActivity(
                                                Intent(
                                                    this@MainActivity,
                                                    SecondActivity::class.java
                                                ).putExtra("data", Gson().toJson(uiResponse))
                                            )
                                        } else {
                                            showToast("Please Insert Data")
                                        }
                                    }
                                }
                                else -> {


                                }
                            }
                        }
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainActivityViewModel.errorMessage.collect {
                        showToast(it)
                    }
                }
            }

            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainActivityViewModel.loadingStatus.collectLatest { loadingStatus ->
                        binding.srData.isRefreshing =
                            loadingStatus == true
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()

    }
}