package com.example.myapplication.adapter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel :ViewModel() {
    val token = MutableLiveData<String>()
}