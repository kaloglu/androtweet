package com.zsk.androtweet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.kaloglu.library.ui.viewmodel.mvi.State
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterException
import com.zsk.androtweet.BR
import com.zsk.androtweet.R
import com.zsk.androtweet.databinding.LoginFragmentBinding
import com.zsk.androtweet.interfaces.twittercallback.LoginCallback
import com.zsk.androtweet.models.User
import com.zsk.androtweet.mvi.LoginEvent
import com.zsk.androtweet.mvi.LoginState
import com.zsk.androtweet.twittercallback.TwitterSessionCallback
import com.zsk.androtweet.ui.fragments.base.ATBaseFragment
import com.zsk.androtweet.viewmodels.LoginViewModel
import com.zsk.androtweet.viewmodels.LoginViewModelFactory
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : ATBaseFragment<LoginFragmentBinding, LoginViewModel>(R.layout.login_fragment) {
    override val viewModel: LoginViewModel by activityViewModels { LoginViewModelFactory(lifecycle) }

    private val loginCallback = object : LoginCallback {
        override fun login(user: User) = pushEvent(LoginEvent.LogIn(user))
        override fun failure(exception: TwitterException?) = pushEvent(LoginEvent.LogOut)
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        viewDataBinding.setVariable(BR.callback, TwitterSessionCallback(loginCallback))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            twitterLogin.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStateSuccess(state: State.Done) {
        when (state) {
            is LoginState.Authenticated -> {
                Toast.makeText(context, "Authenticated : livedata ok", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTweetListFragment())
            }
        }
    }

    fun pushEvent(event: LoginEvent) = viewModel.postEvent(event)

}
