package com.zsk.androtweet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterException
import com.zsk.androtweet.BR
import com.zsk.androtweet.R
import com.zsk.androtweet.databinding.LoginFragmentBinding
import com.zsk.androtweet.interfaces.twittercallback.LoginCallback
import com.zsk.androtweet.models.User
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.twittercallback.TwitterSessionCallback
import com.zsk.androtweet.ui.fragments.base.ATBaseFragment
import com.zsk.androtweet.usecases.GetUserUseCase
import com.zsk.androtweet.viewmodels.LoginViewModel
import com.zsk.androtweet.viewmodels.LoginViewModelFactory
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi

@FlowPreview
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class LoginFragment : ATBaseFragment<LoginFragmentBinding, LoginViewModel>(R.layout.login_fragment) {
    override val viewModel: LoginViewModel by activityViewModels { LoginViewModelFactory(GetUserUseCase()) }

    private val loginCallback = object : LoginCallback {
        override fun login(user: User) {
            Log.i("Login", "SUCCESS")
            viewModel.login(user)
        }

        override fun failure(exception: TwitterException?) {
            Log.e("Login", "ERROR: ${exception?.localizedMessage ?: ""}", exception)
            viewModel.logout()
        }
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        viewDataBinding.setVariable(BR.loginCallBack, TwitterSessionCallback(loginCallback))
    }

    override fun getBindingVariable() = BR.loginViewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            twitterLogin.onActivityResult(requestCode, resultCode, data)
    }

    override fun LoginViewModel.observeViewModel() {
        state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LoginState.Authenticated -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTweetListFragment())
                } // navigate to tweetList
            }
        })
    }


}
