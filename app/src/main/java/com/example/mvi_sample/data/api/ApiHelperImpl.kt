package com.example.mvi_sample.data.api

import com.example.mvi_sample.data.model.User

class ApiHelperImpl(private val apiService: ApiService): ApiHelper {
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}