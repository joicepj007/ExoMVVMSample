package com.android.dazncodingchallenge.domain.repository

import com.android.dazncodingchallenge.domain.Schedule

/**
 * To make an interaction between [ScheduleRepositoryImp] & [GetScheduleUseCase]
 * */
interface ScheduleRepository {

    suspend fun getScheduleList(): List<Schedule>
}