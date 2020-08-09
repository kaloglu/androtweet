package com.zsk.androtweet.ui.fragments.base

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.kaloglu.library.databinding4vm.BindableViewModel
import com.kaloglu.library.databinding4vm.BindingFragment
import com.kaloglu.library.viewmodel.mvi.State
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.zsk.androtweet.BR
import com.zsk.androtweet.ui.activities.base.ATBaseActivity
import com.zsk.androtweet.utils.ContextProviders
import com.zsk.androtweet.utils.extensions.navGraphViewModels
import com.zsk.androtweet.viewmodels.LoginViewModel
import com.zsk.androtweet.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.login_dialog_fragment.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
abstract class ATBaseFragment<VDB, VM, S>(resourceLayoutId: Int) : BindingFragment<VDB, VM, S>(resourceLayoutId)
        where  VDB : ViewDataBinding, VM : BindableViewModel<*, S>, S : State {

    override val activity by lazy { getActivity() as ATBaseActivity }
    override val application by lazy { activity.application }

    internal val navController by lazy { activity.navController }

    val loginViewModel: LoginViewModel by navGraphViewModels { ViewModelFactory(lifecycle) }

    override fun getBindingVariable() = BR.viewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            twitterLogin?.onActivityResult(requestCode, resultCode, data)
    }

    override fun observeViewModel(viewLifecycleOwner: LifecycleOwner) {
        with(viewModel) {
            onStateInit()
            GlobalScope.launch(ContextProviders.instance.Main, CoroutineStart.UNDISPATCHED) {
                stateFlow
                        .filterNotNull()
                        .collectLatest {
                            when (it) {
                                is State.Failure -> onStateFailure(it.error)
                                is State.Init -> onStateInit()
                                is State.Loading -> onStateLoading()
                                is State.Empty -> onStateEmpty()
                                is State.Done -> onStateDone(it)
                                else -> onState(it)
                            }
                        }
            }
        }
    }

}
