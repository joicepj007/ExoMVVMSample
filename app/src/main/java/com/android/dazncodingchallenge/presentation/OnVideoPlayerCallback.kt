package com.android.dazncodingchallenge.presentation

import com.android.dazncodingchallenge.domain.Events

/**
 * To make an interaction between [MainActivity] & its children
 * */
interface OnVideoPlayerCallback {

    fun navigateToVideoPlayerPage(manufactureData: Events)

}