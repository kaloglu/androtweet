package com.zsk.androtweet.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.R
import com.zsk.androtweet.adapters.TimelineAdapter
import com.zsk.androtweet.databinding.TweetListFragmentBinding
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.ui.fragments.base.ATBaseFragment
import com.zsk.androtweet.utils.navGraphViewModels
import com.zsk.androtweet.viewmodels.TweetListViewModel
import com.zsk.androtweet.viewmodels.TweetListViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TweetListFragment : ATBaseFragment<TweetListFragmentBinding, TweetListViewModel>(R.layout.tweet_list_fragment) {
    override val viewModel: TweetListViewModel by navGraphViewModels { TweetListViewModelFactory(lifecycle) }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        viewDataBinding.adapter = TimelineAdapter()
        loginViewModel.stateLiveData.observe(viewLifecycleOwner, Observer { loginState ->
            when (loginState) {
                is LoginState.UnAuthenticated -> findNavController().navigate(R.id.loginDialogFragment)
                is LoginState.Authenticated -> viewModel.postEvent(TweetListEvent.GetTweetList(loginState.user.id))
            }
        })
    }

}
