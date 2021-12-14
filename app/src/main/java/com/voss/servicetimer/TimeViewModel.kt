package com.voss.servicetimer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeViewModel : ViewModel() {
    val secondLiveData = MutableLiveData<Long>()

    init {
        secondLiveData.value = 0
    }




}