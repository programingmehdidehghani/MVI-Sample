package com.example.mvi_sample.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvi_sample.data.api.ApiHelper

class ViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom())
    }
}