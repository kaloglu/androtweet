package com.zsk.androtweet.ui.fragments

import android.app.Dialog
import com.twitter.sdk.android.core.TwitterException
import com.zsk.androtweet.BR
import com.zsk.androtweet.R
import com.zsk.androtweet.databinding.LoginDialogFragmentBinding
import com.zsk.androtweet.interfaces.LoginCallback
import com.zsk.androtweet.interfaces.twittercallback.TwitterSessionCallback
import com.zsk.androtweet.models.User
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.ui.fragments.base.ATBaseDialogFragment
import com.zsk.androtweet.viewmodels.LoginViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LoginDialogFragment
    : ATBaseDialogFragment<LoginDialogFragmentBinding, LoginViewModel, LoginState>(R.layout.login_dialog_fragment) {
    override val viewModel by lazy { loginViewModel }

    private val loginCallback = object : LoginCallback {
        override fun login(user: User) = viewModel.postEvent(LoginEvent.LogIn(user))
        override fun failure(exception: TwitterException?) = viewModel.postEvent(LoginEvent.LogOut)
    }

    override fun initUserInterface(dialog: Dialog) {
        viewDataBinding.setVariable(BR.callback, TwitterSessionCallback(loginCallback))
        viewModel.title = "You should Login with your twitter Accounts!"
    }

    override fun onState(state: LoginState) {
        when (state) {
            is LoginState.Authenticated -> dismiss()
        }
    }

}
