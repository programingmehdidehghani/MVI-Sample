package com.example.mvi_sample.data.api

import com.example.mvi_sample.data.model.User

interface ApiHelper {

    suspend fun getUsers(): List<User>
}