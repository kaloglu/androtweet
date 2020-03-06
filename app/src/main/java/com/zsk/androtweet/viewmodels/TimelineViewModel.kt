package com.zsk.androtweet.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaloglu.library.ui.utils.Resource
import com.zsk.androtweet.interfaces.GetTweetsUseCases
import com.zsk.androtweet.interfaces.InsertTweetsUseCases
import com.zsk.androtweet.interfaces.RemoveTweetsUseCases
import com.zsk.androtweet.models.Tweet

class TimelineViewModel(
        private val getTweetsUseCases: GetTweetsUseCases,
        private val removeTweetsUseCases: RemoveTweetsUseCases,
        private val insertTweetsUseCases: InsertTweetsUseCases
) : ViewModel() {

    init {
        loadTimeline()
    }

    val timeline = MutableLiveData<List<Tweet>>()

    private fun loadTimeline() {
        handleResult(getTweetsUseCases.invoke())
    }

    private fun handleResult(resource: Resource<List<Tweet>>) {
        when (resource) {
            is Resource.Success -> timeline.postValue(resource.body)
            else -> Log.e("Error", resource.error?.message ?: "Hata!")
        }
    }
}
