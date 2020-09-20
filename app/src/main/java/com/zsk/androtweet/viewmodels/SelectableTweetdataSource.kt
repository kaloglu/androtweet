package com.zsk.androtweet.viewmodels

/*

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class TweetFromDaoDataSource(private val repository: TweetListRepository) : ItemKeyedDataSource<Long, TweetFromDao>() {
    override fun loadInitial(initialParams: LoadInitialParams<Long>, loadInitialCallback: LoadInitialCallback<TweetFromDao>) =
            repository.getUserTimelineNext(initialParams.requestedLoadSize, initialParams.requestedInitialKey, loadInitialCallback)

    override fun loadAfter(params: LoadParams<Long>, loadCallback: LoadCallback<TweetFromDao>) =
            repository.getUserTimelinePrevious(params.requestedLoadSize, params.key, loadCallback)

    override fun loadBefore(params: LoadParams<Long>, loadCallback: LoadCallback<TweetFromDao>) =
            repository.getUserTimelineNext(params.requestedLoadSize, params.key, loadCallback)

    override fun getKey(p0: TweetFromDao): Long = p0.tweet.id
}
*/

