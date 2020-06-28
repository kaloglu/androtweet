package com.zsk.androtweet.ui.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.kaloglu.library.ui.setItemLongClickListener
import com.zsk.androtweet.R
import com.zsk.androtweet.adapters.TimelineAdapter
import com.zsk.androtweet.databinding.TweetListFragmentBinding
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.ui.fragments.base.ATBaseFragment
import com.zsk.androtweet.utils.extensions.navGraphViewModels
import com.zsk.androtweet.utils.extensions.setItemClickListener
import com.zsk.androtweet.viewmodels.TweetListViewModel
import com.zsk.androtweet.viewmodels.TweetListViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListFragment : ATBaseFragment<TweetListFragmentBinding, TweetListViewModel>(R.layout.tweet_list_fragment) {
    override val viewModel: TweetListViewModel by navGraphViewModels { TweetListViewModelFactory(lifecycle) }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        setDatabindingParams()
        setLoginViewModelObserver()
    }

    private fun setDatabindingParams() {
        viewDataBinding.tweetsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewDataBinding.adapter = TimelineAdapter()
                .setItemClickListener { item, _ ->
                    item.isSelected = !item.isSelected
                }
                .setItemLongClickListener { _, _ ->
                    viewModel.selectAllItem()
                    true
                }
    }

    private fun setLoginViewModelObserver() {
        loginViewModel.stateLiveData.observe(viewLifecycleOwner, Observer { loginState ->
            when (loginState) {
                is LoginState.UnAuthenticated -> findNavController().navigate(R.id.loginDialogFragment)
                is LoginState.Authenticated -> viewModel.postEvent(TweetListEvent.GetTweetList(loginState.user.id))
            }
        })
    }

}
