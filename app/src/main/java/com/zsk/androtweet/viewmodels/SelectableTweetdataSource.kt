package com.zsk.androtweet.viewmodels

import androidx.paging.ItemKeyedDataSource
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.repositories.TweetListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class SelectableTweetdataSource(private val repository: TweetListRepository) : ItemKeyedDataSource<Long, SelectableTweet>() {
    override fun loadInitial(initialParams: LoadInitialParams<Long>, loadInitialCallback: LoadInitialCallback<SelectableTweet>) =
            repository.getUserTimelineNext(initialParams.requestedLoadSize,initialParams.requestedInitialKey, loadInitialCallback)

    override fun loadAfter(params: LoadParams<Long>, loadCallback: LoadCallback<SelectableTweet>) =
            repository.getUserTimelinePrevious(params.requestedLoadSize,params.key, loadCallback)

    override fun loadBefore(params: LoadParams<Long>, loadCallback: LoadCallback<SelectableTweet>) =
            repository.getUserTimelineNext(params.requestedLoadSize,params.key, loadCallback)

    override fun getKey(p0: SelectableTweet): Long = p0.tweet.id
}