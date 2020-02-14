package com.zsk.androtweet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zsk.androtweet.interfaces.GetTweetsUseCases
import com.zsk.androtweet.interfaces.InsertTweetsUseCases
import com.zsk.androtweet.interfaces.RemoveTweetsUseCases

class TimelineViewModelFactory(
        private val getTweetsUseCases: GetTweetsUseCases,
        private val removeTweetsUseCases: RemoveTweetsUseCases,
        private val insertTweetsUseCases: InsertTweetsUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TimelineViewModel(getTweetsUseCases, removeTweetsUseCases, insertTweetsUseCases) as T
    }
}