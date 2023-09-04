package com.example.mvi_sample.data.repository

import com.example.mvi_sample.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}