package com.zsk.androtweet.ui.fragments

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.kaloglu.library.ktx.isNotNullOrEmpty
import com.kaloglu.library.ui.setItemClickListener
import com.kaloglu.library.ui.setItemLongClickListener
import com.zsk.androtweet.R
import com.zsk.androtweet.adapters.TimelineAdapter
import com.zsk.androtweet.databinding.TweetListFragmentBinding
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.ui.fragments.base.ATBaseFragment
import com.zsk.androtweet.utils.extensions.navGraphViewModels
import com.zsk.androtweet.viewmodels.TweetListViewModel
import com.zsk.androtweet.viewmodels.TweetListViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListFragment : ATBaseFragment<TweetListFragmentBinding, TweetListViewModel, TweetListState>(R.layout.tweet_list_fragment) {
    override val viewModel: TweetListViewModel by navGraphViewModels { TweetListViewModelFactory(lifecycle) }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        setDatabindingParams()
        setLoginViewModelObserver()
    }

    private fun setDatabindingParams() {
        viewDataBinding.tweetsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewDataBinding.adapter = createTimeLineAdapter()
    }

    private fun createTimeLineAdapter() =
            TimelineAdapter().apply {
                setItemClickListener { item, _ ->
                    if (item.result.isNotNullOrEmpty())
                        return@setItemClickListener

                    item.isSelected = !item.isSelected
                    viewModel.postEvent(TweetListEvent.ToggleSelectItem(item))
                }
                setItemLongClickListener { _, _ ->
                    viewModel.postEvent(TweetListEvent.ToggleSelectAllItem)
                    true
                }
            }

    private fun setLoginViewModelObserver() {
        loginViewModel.stateFlow.onEach { loginState ->
            when (loginState) {
                is LoginState.UnAuthenticated -> findNavController().navigate(R.id.loginDialogFragment)
                is LoginState.Authenticated -> {
                    viewModel.postEvent(TweetListEvent.GetTweetList(loginState.user.id))
                }
            }
        }.launchIn(lifecycleScope)
    }

}

