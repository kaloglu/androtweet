package com.zsk.androtweet.ui.fragments

import android.os.Bundle
import com.kaloglu.library.ui.viewmodel.databinding.BindingFragment
import com.zsk.androtweet.R
import com.zsk.androtweet.databinding.LoginFragmentBinding
import com.zsk.androtweet.viewmodels.LoginViewModel

class LoginFragment : BindingFragment<LoginFragmentBinding, LoginViewModel>(R.layout.login_fragment) {

    override val viewModelClass: Class<LoginViewModel>
        get() = LoginViewModel::class.java

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun initUserInterface(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            viewModel.testText = "Bunu da değiştir!"
        }, 10000)
    }

}
