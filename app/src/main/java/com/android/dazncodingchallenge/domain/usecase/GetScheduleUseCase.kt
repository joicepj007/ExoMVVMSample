package com.android.dazncodingchallenge.domain.usecase

import com.android.dazncodingchallenge.domain.Schedule
import com.android.dazncodingchallenge.domain.repository.ScheduleRepository
import javax.inject.Inject

/**
 * An interactor that calls the actual implementation of [ScheduleListViewModel](which is injected by DI)
 * it handles the response that returns data &
 * contains a list of actions, event steps
 */
class GetScheduleUseCase @Inject constructor(private val repository: ScheduleRepository) {

    suspend fun getScheduleData(): List<Schedule> {
        return repository.getScheduleList()
    }
}