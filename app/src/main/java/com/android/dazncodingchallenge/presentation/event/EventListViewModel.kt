package com.android.dazncodingchallenge.presentation.event

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.dazncodingchallenge.domain.Events
import com.android.dazncodingchallenge.domain.usecase.GetEventsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**To store & manage UI-related data in a lifecycle conscious way!
 * this class allows data to survive configuration changes such as screen rotation
 * by interacting with [GetEventsUseCase]
 *
 * */
class EventListViewModel @ViewModelInject constructor(private val getEventsUseCase: GetEventsUseCase) : ViewModel() {

    val eventsReceivedLiveData = MutableLiveData<List<Events>?>()
    val isLoad = MutableLiveData<Boolean>()
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        isLoad.value = false
    }

    fun loadEventsList(){
        coroutineScope.launch {
            try {
                getEventsUseCase.getEventsData().let { events ->
                    isLoad.value = true
                    eventsReceivedLiveData.postValue(events)
                }
            } catch (exception: Exception) {
            }
        }
    }
}