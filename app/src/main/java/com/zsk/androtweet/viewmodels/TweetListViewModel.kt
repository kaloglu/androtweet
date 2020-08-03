package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.kaloglu.library.viewmodel.BaseViewModel
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.SelectableTweet
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.repositories.TweetListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TweetListViewModel(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance), LifecycleObserver {

    private lateinit var loginViewModel: LoginViewModel

    @get:Bindable
    var list by bindable(emptyList<SelectableTweet>()) { o, n ->
        n.size
    }

    @get:Bindable
    var hasSelected: Boolean by bindable(false)

    val loginStateFlow
        get() = when {
            ::loginViewModel.isInitialized -> loginViewModel.stateFlow
            else -> null
        }

    init {
        viewModelScope.launch {
            repository.tweetFlow
                    .collect { (selectableTweetList, error) ->
                        when {
                            selectableTweetList != null -> list = selectableTweetList
                            error != null -> postState(TweetListState.Failure(error))
                        }

                    }

        }
    }

    override suspend fun onEvent(event: TweetListEvent) {
        when (event) {
            is TweetListEvent.GetTweetList -> {
                list = emptyList()
                repository.initialList(loginViewModel.user.id)
            }
            is TweetListEvent.UpdateList -> list = event.tweetList
            is TweetListEvent.ToggleSelectItem -> checkHasSelected()
            is TweetListEvent.ToggleSelectAllItem -> toggleSelectAllItem()
        }
    }

    override fun <VM : BaseViewModel<*, *>> attachViewModel(vararg viewModels: VM) {
        viewModels.forEach { viewModel ->
            when (viewModel) {
                is LoginViewModel -> loginViewModel = viewModel
            }
        }
    }

    fun deleteSelectedTweets() {
//        repository.destroyTweets(list.filter { it.isSelected })
    }

    private fun toggleSelectAllItem() {
        list = list
//        map { it.isSelected = !hasSelected }
        postState(TweetListState.SelectedItem(!hasSelected))
    }

    private fun checkHasSelected(default: Boolean = false) {
        hasSelected = list.filter { it.result.isEmpty() }.find { it.isSelected }?.isSelected
                ?: default
    }

}
