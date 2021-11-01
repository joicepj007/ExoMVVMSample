package com.android.dazncodingchallenge.data.repository

import com.android.dazncodingchallenge.data.RetrofitService
import com.android.dazncodingchallenge.domain.Events
import com.android.dazncodingchallenge.domain.repository.EventRepository

/**
 * This repository is responsible for
 * fetching data[Events] from server
 * */

class EventRepositoryImp(
    private val retrofitService: RetrofitService
) :
    EventRepository {
    override suspend fun getEventsList(): List<Events> {
        return retrofitService.getEvents()
    }
}