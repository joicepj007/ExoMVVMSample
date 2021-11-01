package com.android.dazncodingchallenge.domain.repository

import com.android.dazncodingchallenge.domain.Events

/**
 * To make an interaction between [EventRepositoryImp] & [GetEventsUseCase]
 * */
interface EventRepository {

    suspend fun getEventsList(): List<Events>
}