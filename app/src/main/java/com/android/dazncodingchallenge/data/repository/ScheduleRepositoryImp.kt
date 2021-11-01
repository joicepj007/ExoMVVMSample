package com.android.dazncodingchallenge.data.repository

import com.android.dazncodingchallenge.data.RetrofitService
import com.android.dazncodingchallenge.domain.Schedule
import com.android.dazncodingchallenge.domain.repository.ScheduleRepository

/**
 * This repository is responsible for
 * fetching data[Schedule] from server
 * */

class ScheduleRepositoryImp(
    private val retrofitService: RetrofitService
) :
    ScheduleRepository {
    override suspend fun getScheduleList(): List<Schedule> {
        return retrofitService.getSchedule()
    }
}