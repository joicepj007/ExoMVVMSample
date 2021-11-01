package com.android.dazncodingchallenge.data


import com.android.dazncodingchallenge.domain.Events
import com.android.dazncodingchallenge.domain.Schedule
import retrofit2.http.GET

interface RetrofitService {

    @GET("getEvents")
    suspend fun getEvents(): List<Events>

    @GET("getSchedule")
    suspend fun getSchedule(): List<Schedule>
}