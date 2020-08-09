package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.filter
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.bindable
import com.kaloglu.library.viewmodel.BaseViewModel
import com.zsk.androtweet.AndroTweetApp
import com.zsk.androtweet.models.TweetFromDao
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.mvi.TweetListEvent
import com.zsk.androtweet.mvi.TweetListState
import com.zsk.androtweet.repositories.TweetListRepository
import com.zsk.androtweet.utils.extensions.RoomExtensions.onIO
import com.zsk.androtweet.utils.extensions.RoomExtensions.onUndispatchedIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class TweetListViewModel(
        private val repository: TweetListRepository = TweetListRepository.getInstance()
) : BindableViewModel<TweetListEvent, TweetListState>(AndroTweetApp.instance), LifecycleObserver {

//    private lateinit var loginViewModel: LoginViewModel

    /*@get:Bindable
    var list: PagingData<TweetFromDao> = TODO()
*/
    @get:Bindable
    var selectedList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var failedList by bindable(listOf<TweetFromDao>())

    @get:Bindable
    var deletedList by bindable(listOf<TweetFromDao>())

    /*private val loginStateFlow
        get() = when {
            ::loginViewModel.isInitialized -> loginViewModel.stateFlow
            else -> null
        }*/

    override suspend fun onEvent(event: TweetListEvent) {
        when (event) {
            is TweetListEvent.GetTweetList -> getTweetList(event)
//            is TweetListEvent.UpdateList -> list = event.tweetList
            is TweetListEvent.ToggleSelectItem -> checkHasSelected()
            is TweetListEvent.ToggleSelectAllItem -> toggleSelectAllItem()
        }
    }

    private suspend fun getTweetList(event: TweetListEvent.GetTweetList) {
        viewModelScope.onIO {
            repository
                    .get(event.user.id)
                    .collectLatest {
                        postState(TweetListState.UpdateUI(it))
                    }
        }
    }

    override fun <VM : BaseViewModel<*, *>> attachViewModel(vararg viewModels: VM) {
        viewModels.forEach { viewModel ->
            when (viewModel) {
                is LoginViewModel -> onAttachLoginViewModel(viewModel)
            }
        }
    }

    private fun onAttachLoginViewModel(viewModel: LoginViewModel) {
        viewModelScope.onIO {
            viewModel.stateFlow
                    .collect { loginState ->
                        when (loginState) {
                            is LoginState.UnAuthenticated -> postState(TweetListState.NeedLogin)
                            is LoginState.Authenticated -> postEvent(TweetListEvent.GetTweetList(loginState.user))
                        }
                    }
        }
    }

    fun deleteSelectedTweets() {
        viewModelScope.onIO {
//            repository.delete(list.filter { it.isSelected })
        }
    }

    private fun toggleSelectAllItem() {
//        list.mapSync {
//            it.isSelected = selectedList.contains(it)
//        }
//        postState(TweetListState.SelectedItem(!hasSelected))
    }

    private fun checkHasSelected(default: Boolean = false) {
//        hasSelected = false
    }

}
