package com.zsk.androtweet.repositories

import androidx.annotation.WorkerThread
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import com.kaloglu.library.ui.interfaces.Repository
import com.twitter.sdk.android.tweetui.UserTimeline
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.utils.Constants
import com.zsk.androtweet.viewmodels.SelectableTweetdataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.asExecutor

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class TweetListRepository : Repository<List<SelectableTweet>> {
    private var userId: Long? = null

    private val providePagingConfig
        get() = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(Constants.prefetchDistance)
                .setInitialLoadSizeHint(Constants.initialLoadSizeHint)
                .setPageSize(Constants.pageSize)
                .build()

    @WorkerThread
    override suspend fun delete(entity: List<SelectableTweet>) = Unit

    @WorkerThread
    override suspend fun update(entity: List<SelectableTweet>) = Unit

    @WorkerThread
    override suspend fun insert(entity: List<SelectableTweet>) = Unit

    fun getUserTimelinePrevious(pageSize: Int, maxposition: Long? = null, callback: ItemKeyedDataSource.LoadCallback<SelectableTweet>) {
        if (userId == null) return

        UserTimeline.Builder()
                .userId(userId)
                .includeReplies(false)
                .includeRetweets(false)
                .maxItemsPerRequest(pageSize)
                .build()
                .previous(maxposition, TweetTimelineResultCallback(callback))
    }

    fun getUserTimelineNext(pageSize: Int, minPosition: Long? = null, callback: ItemKeyedDataSource.LoadCallback<SelectableTweet>) {
        if (userId == null) return

        UserTimeline.Builder()
                .userId(userId)
                .includeReplies(false)
                .includeRetweets(false)
                .maxItemsPerRequest(pageSize)
                .build()
                .next(minPosition, TweetTimelineResultCallback(callback))
    }

    fun initUserTimeline(): PagedList<SelectableTweet> {
        return PagedList.Builder(SelectableTweetdataSource(this), providePagingConfig)
                .setNotifyExecutor(Dispatchers.Main.asExecutor())
                .setFetchExecutor(Dispatchers.IO.asExecutor())
                .build()
    }

    fun setUserId(id: Long) {
        userId = id
    }

    companion object {

        @Volatile
        private lateinit var INSTANCE: TweetListRepository

        fun getInstance(): TweetListRepository {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = TweetListRepository()
                }
            }
            return INSTANCE
        }

    }

}
