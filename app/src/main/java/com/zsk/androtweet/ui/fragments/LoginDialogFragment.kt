package com.zsk.androtweet.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.view.ViewGroup
import android.view.Window
import com.kaloglu.library.viewmodel.mvi.State
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterException
import com.zsk.androtweet.BR
import com.zsk.androtweet.R
import com.zsk.androtweet.databinding.LoginDialogFragmentBinding
import com.zsk.androtweet.interfaces.twittercallback.LoginCallback
import com.zsk.androtweet.models.User
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.twittercallback.TwitterSessionCallback
import com.zsk.androtweet.ui.fragments.base.ATBaseDialogFragment
import com.zsk.androtweet.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.login_dialog_fragment.view.*

class LoginDialogFragment
    : ATBaseDialogFragment<LoginDialogFragmentBinding, LoginViewModel>(R.layout.login_dialog_fragment) {
    override val viewModel by lazy { loginViewModel }

    private val loginCallback = object : LoginCallback {
        override fun login(user: User) = pushEvent(LoginEvent.LogIn(user))
        override fun failure(exception: TwitterException?) = pushEvent(LoginEvent.LogOut)
    }

    override fun setDialogStyle(dialogWindow: Window) {
        dialogWindow.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialogWindow.setDimAmount(0.9f)
    }

    override fun initUserInterface(dialog: Dialog) {
        viewDataBinding.setVariable(BR.callback, TwitterSessionCallback(loginCallback))
        viewModel.title = "You should Login with your twitter Accounts!"
    }

    override fun onStateSuccess(state: State.Success) {
        when (state) {
            is LoginState.Authenticated -> dismiss()
        }
    }

    fun pushEvent(event: LoginEvent) = viewModel.postEvent(event)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            containerView.twitterLogin?.onActivityResult(requestCode, resultCode, data)
    }


}
