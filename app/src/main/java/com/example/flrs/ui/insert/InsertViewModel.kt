package com.example.flrs.ui.insert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InsertViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _text = MutableLiveData<String>().apply {
        value = "This is insert Fragment"
    }
    val text: LiveData<String> = _text
}