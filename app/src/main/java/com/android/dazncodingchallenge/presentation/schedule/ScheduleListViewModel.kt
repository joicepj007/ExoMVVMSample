package com.android.dazncodingchallenge.presentation.schedule

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.dazncodingchallenge.domain.Schedule
import com.android.dazncodingchallenge.domain.usecase.GetScheduleUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**To store & manage UI-related data in a lifecycle conscious way!
 * this class allows data to survive configuration changes such as screen rotation
 * by interacting with [GetScheduleUseCase]
 *
 * */
class ScheduleListViewModel @ViewModelInject constructor(private val getScheduleUseCase: GetScheduleUseCase) : ViewModel() {

    val scheduleReceivedLiveData = MutableLiveData<List<Schedule>?>()
    val isLoad = MutableLiveData<Boolean>()
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        isLoad.value = false
    }

    fun loadScheduleList(){
        coroutineScope.launch {
            try {
                getScheduleUseCase.getScheduleData().let { events ->
                    isLoad.value = true
                    scheduleReceivedLiveData.postValue(events)
                }
            } catch (exception: Exception) {
            }
        }
    }
}