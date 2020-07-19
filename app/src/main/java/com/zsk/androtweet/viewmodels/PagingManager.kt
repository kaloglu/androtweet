package com.zsk.androtweet.viewmodels

import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import com.kaloglu.library.ui.RecyclerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.Executor

interface PagingManager<K : Any, RI> where RI : RecyclerItem {
    val config: PagedList.Config

    val notifyExecutor: Executor
        get() = Dispatchers.Main.asExecutor()
    val fetchExecutor: Executor
        get() = Dispatchers.IO.asExecutor()

    fun key(item: RI): K = item.getID()

    fun initial(initialParams: ItemKeyedDataSource.LoadInitialParams<K>, loadInitialCallback: ItemKeyedDataSource.LoadInitialCallback<RI>)

    fun before(params: ItemKeyedDataSource.LoadParams<K>, loadCallback: ItemKeyedDataSource.LoadCallback<RI>)

    fun after(params: ItemKeyedDataSource.LoadParams<K>, loadCallback: ItemKeyedDataSource.LoadCallback<RI>)

    fun getDataSource(): ItemKeyedDataSource<K, RI> = object : ItemKeyedDataSource<K, RI>() {
        override fun loadInitial(initialParams: LoadInitialParams<K>, loadInitialCallback: LoadInitialCallback<RI>) =
                initial(initialParams, loadInitialCallback)

        override fun loadAfter(params: LoadParams<K>, loadCallback: LoadCallback<RI>) =
                after(params, loadCallback)

        override fun loadBefore(params: LoadParams<K>, loadCallback: LoadCallback<RI>) =
                before(params, loadCallback)

        override fun getKey(item: RI): K = key(item)
    }

    fun <RI : RecyclerItem> getPagedList(items: List<RI>) =
            PagedList.Builder(getDataSource(), config)
                    .setNotifyExecutor(notifyExecutor)
                    .setFetchExecutor(fetchExecutor)
                    .build()


}
