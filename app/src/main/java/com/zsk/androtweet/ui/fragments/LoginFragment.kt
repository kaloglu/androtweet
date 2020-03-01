package com.zsk.androtweet.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.kaloglu.library.ui.viewmodel.databinding.BindingFragment
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.zsk.androtweet.BR
import com.zsk.androtweet.R
import com.zsk.androtweet.databinding.LoginFragmentBinding
import com.zsk.androtweet.interfaces.twittercallback.LoginCallback
import com.zsk.androtweet.states.LoginState
import com.zsk.androtweet.twittercallback.TwitterSessionCallback
import com.zsk.androtweet.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BindingFragment<LoginFragmentBinding, LoginViewModel>(R.layout.login_fragment) {

    private val loginCallback = object : LoginCallback {
        override fun login() {
            Log.i("Login", "SUCCESS")
//            findNavController().popBackStack(R.id.tweetListFragment, false)
        }
    }

    override val viewModelClass: Class<LoginViewModel>
        get() = LoginViewModel::class.java

    companion object {
        fun newInstance() = LoginFragment()
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

