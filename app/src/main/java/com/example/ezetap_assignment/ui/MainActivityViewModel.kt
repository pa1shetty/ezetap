package com.example.ezetap_assignment.ui

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.NetworkCalls
import com.example.network.network.response.ErrorMessage
import com.example.network.network.response.Status
import com.example.network.network.response.UiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val networkCalls: NetworkCalls) :
    ViewModel() {
    private val _uiResponse = MutableStateFlow(UiResponse())
    val uiResponse: StateFlow<UiResponse> = _uiResponse.asStateFlow()
    val bitmapStore = MutableLiveData<Bitmap>()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private var _loadingStatus = MutableStateFlow(false)
    val loadingStatus = _loadingStatus.asStateFlow()

    init {
        getResponseUI()
    }

    fun getResponseUI() {
        viewModelScope.launch {
            _loadingStatus.emit(true)
            try {
                val response = networkCalls.getUi()
                if (response.status == Status.SUCCESS) {
                    _uiResponse.value = response.uiResponse as UiResponse
                    uiResponse.value.logo_url?.let { getImage(it) }
                } else {
                    _errorMessage.emit(response.errorMessage)
                }
            } catch (e: Exception) {
                _errorMessage.emit(ErrorMessage.ERROR.message)
            }
            _loadingStatus.value = false
        }
    }

    private fun getImage(url: String) {
        viewModelScope.launch {
            _loadingStatus.value = true
            try {
                val response = networkCalls.getImage(url)
                if (response.status == Status.SUCCESS) {
                    bitmapStore.value = response.uiResponse as Bitmap
                } else {
                    _errorMessage.emit(response.errorMessage)
                }
            } catch (e: Exception) {
                _errorMessage.emit(ErrorMessage.ERROR.message)
            }
            _loadingStatus.value = false
        }
    }

    fun updateValue(index: Int, value: String) {
        _uiResponse.value.uiData?.get(index)?.value = value
    }
}