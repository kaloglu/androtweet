package com.zsk.androtweet.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kaloglu.library.ui.viewmodel.mvi.State
import com.zsk.androtweet.R
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

    companion object {
        fun newInstance() = TweetListFragment()
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        loginViewModel.stateLiveData.observe(viewLifecycleOwner, Observer { userState ->
            when (userState) {
                is LoginState.UnAuthenticated -> findNavController().navigate(R.id.loginDialogFragment)
                is LoginState.Authenticated -> viewModel.postEvent(TweetListEvent.FetchRemoteData(userState.user.id))
            }
        })
    }

    override fun onStateLoading(state: State.Loading) {
        showToast("Loading!!!!!")
        Log.i(viewTag, state.javaClass.simpleName)
    }

    override fun onStateEmpty(state: State.Empty) {
        showToast("Empty!!!!!")
        Log.i(viewTag, state.javaClass.simpleName)
    }

    override fun onStateSuccess(state: State.Success) {
        showToast("Success!!!!!")
        Log.i(viewTag, state.javaClass.simpleName)
    }
}
