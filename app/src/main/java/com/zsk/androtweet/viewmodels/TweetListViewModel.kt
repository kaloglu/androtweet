package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.kaloglu.library.viewmodel.BaseViewModel
import com.kaloglu.library.viewmodel.mvi.Event
import com.kaloglu.library.viewmodel.mvi.State
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.utils.extensions.RoomExtensions.onIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
class TweetListViewModel(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance), LifecycleObserver {

    override val idleState: State.Idle = TweetListState.Idle
    override val idleEvent: Event.Idle = TweetListEvent.Idle

    @get:Bindable
    var tweetList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var selectedList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var failedList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var deletedList by bindable(listOf<TweetFromDao>())

    override fun <VM : BaseViewModel<*, *>> attachViewModel(vararg viewModels: VM) {
        viewModels.forEach { viewModel ->
            when (viewModel) {
                is LoginViewModel -> onAttachLoginViewModel(viewModel)
            }
        }
    }

    suspend fun getTweetList() {
        viewModelScope.onIO {
            repository.clearDao()
            repository.getTweets()
                    .collectLatest {
                        if (it != null)
                            tweetList = it
                    }
        }
    }

    fun deleteSelectedTweets() {
        viewModelScope.onIO {
            deletedList = repository.deleteWithReturn(selectedList)
        }
    }

    fun getTweetListFromRemote() {
        viewModelScope.onIO {
            val tweetsRemote = repository.getTweetsRemote()

            if (tweetsRemote)
                postState(TweetListState.UpdateUI)

        }

    }

    fun toggleSelectItem(item: TweetFromDao) {
        item.isSelected = !item.isSelected
        checkHasSelected()
    }

    fun toggleSelectAllItem(selectAll: Boolean = true) {

        val tempList = tweetList
        val hasNotSelected = selectedList.size != tweetList.size && selectAll
        tempList.map { temp -> temp.isSelected = hasNotSelected }
        tweetList = tempList
        checkHasSelected()

    }

    private fun checkHasSelected() {
        selectedList = tweetList.filter { it.isSelected }
    }

    private fun onAttachLoginViewModel(loginViewModel: LoginViewModel) {
        viewModelScope.onIO {
            loginViewModel.stateFlow
                    .collect { loginState ->
                        when (loginState) {
                            is LoginState.UnAuthenticated -> postState(TweetListState.NeedLogin)
                            is LoginState.Authenticated -> {
                                getTweetList()
                            }
                        }
                    }
        }
    }

}
