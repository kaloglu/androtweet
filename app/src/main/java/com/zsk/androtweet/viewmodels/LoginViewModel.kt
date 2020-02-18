package com.zsk.androtweet.viewmodels

import androidx.databinding.Bindable
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.bindable

class LoginViewModel : BindableViewModel() {

    @get:Bindable
    var testText by bindable("test")

    init {
        checkLogin()
    }

    private fun checkLogin() {

    }


}
