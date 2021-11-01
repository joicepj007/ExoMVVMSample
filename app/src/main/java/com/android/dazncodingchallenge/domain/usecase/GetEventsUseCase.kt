package com.android.dazncodingchallenge.domain.usecase

import com.android.dazncodingchallenge.domain.Events
import com.android.dazncodingchallenge.domain.repository.EventRepository
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [EventListViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetEventsUseCase @Inject constructor(private val repository: EventRepository) {

    suspend fun getEventsData(): List<Events> {
        return repository.getEventsList()
    }
}