package com.android.dazncodingchallenge.presentation.event

import com.android.dazncodingchallenge.domain.Events

/**
 * To make an interaction between [EventListAdapter] & [EventListFragment]
 * */
interface OnEventAdapterListener {

    fun showEventList(eventData: Events)
}