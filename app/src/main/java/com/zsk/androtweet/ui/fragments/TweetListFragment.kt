package com.zsk.androtweet.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.kaloglu.library.ui.models.ErrorModel
import com.kaloglu.library.ui.setItemClickListener
import com.kaloglu.library.ui.setItemLongClickListener
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.R
import com.zsk.androtweet.adapters.TimelineAdapter
import com.zsk.androtweet.databinding.TweetListFragmentBinding
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.ui.fragments.base.ATBaseFragment
import com.zsk.androtweet.utils.Constants
import com.zsk.androtweet.utils.extensions.navGraphViewModels
import com.zsk.androtweet.viewmodels.TweetListViewModel
import com.zsk.androtweet.viewmodels.TweetListViewModelFactory
import kotlinx.android.synthetic.main.tweet_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListFragment : ATBaseFragment<TweetListFragmentBinding, TweetListViewModel, TweetListState>(R.layout.tweet_list_fragment) {

    private lateinit var adapter: TimelineAdapter
    override val viewModel: TweetListViewModel by navGraphViewModels {
        TweetListViewModelFactory(lifecycle, loginViewModel)
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        setDatabindingParams()
    }

    private fun setDatabindingParams() {
        adapter = createTimeLineAdapter()
        viewDataBinding.tweetsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewDataBinding.tweetsRecyclerView.adapter = adapter
        swiperefresh.setOnRefreshListener {
            Log.i(viewTag, "onRefresh called from SwipeRefreshLayout")
            viewModel.postEvent(TweetListEvent.RefreshTweetList)
        }
    }

    private fun createTimeLineAdapter() =
            TimelineAdapter().apply {
                setItemClickListener { item, _ ->
                    viewModel.postEvent(TweetListEvent.ToggleSelectItem(item))
                }
                setItemLongClickListener { _, _ ->
                    viewModel.postEvent(TweetListEvent.ToggleSelectAllItem())
                    true
                }
            }

    override fun onStateDone(it: State.Done) {
        super.onStateDone(it)
        swiperefresh.isRefreshing = false
        if (viewModel.selectedList.isNotEmpty())
            viewModel.postEvent(TweetListEvent.ToggleSelectAllItem(false))
    }

    override fun onStateFailure(error: ErrorModel) {
        super.onStateFailure(error)

        when (error.code) {
            Constants.NEED_LOGIN_ERROR_CODE -> findNavController().navigate(R.id.loginDialogFragment)
        }
    }

    override fun onStateInit() {
        super.onStateInit()
        viewModel.postEvent(TweetListEvent.Idle)
    }
}

