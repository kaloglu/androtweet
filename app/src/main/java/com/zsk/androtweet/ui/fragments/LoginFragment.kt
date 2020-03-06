package com.zsk.androtweet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.kaloglu.library.ui.viewmodel.BaseViewModelFactory
import com.kaloglu.library.ui.viewmodel.getViewModel
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.zsk.androtweet.BR
import com.zsk.androtweet.R
import com.zsk.androtweet.databinding.LoginFragmentBinding
import com.zsk.androtweet.interfaces.twittercallback.LoginCallback
import com.zsk.androtweet.repositories.UserRepository
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.twittercallback.TwitterSessionCallback
import com.zsk.androtweet.ui.fragments.base.ATBaseFragment
import com.zsk.androtweet.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : ATBaseFragment<LoginFragmentBinding, LoginViewModel>(R.layout.login_fragment) {
    override val viewModelFactory by lazy { BaseViewModelFactory(application, UserRepository()) }

    private val loginCallback = object : LoginCallback {
        override fun login() {
            Log.i("Login", "SUCCESS")
        }
    }

    override fun onCreateViewModel() {
        viewModel = viewModelStoreOwner.getViewModel(viewModelFactory, LoginViewModel::class.java)
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        viewDataBinding.setVariable(BR.loginCallBack, TwitterSessionCallback(viewModel.repository, loginCallback))

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is LoginState.Authenticated -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTweetListFragment())
                } // navigate to tweetList
            }
        }
    }

    override fun getBindingVariable() = BR.loginViewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            twitterLogin.onActivityResult(requestCode, resultCode, data)
    }


}

