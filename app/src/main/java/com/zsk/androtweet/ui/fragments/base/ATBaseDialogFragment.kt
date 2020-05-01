package com.zsk.androtweet.ui.fragments.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.dialogFragments.BindingDialogFragment
import com.zsk.androtweet.BR
import com.zsk.androtweet.ui.activities.base.ATBaseActivity
import com.zsk.androtweet.viewmodels.LoginViewModel
import com.zsk.androtweet.viewmodels.LoginViewModelFactory

abstract class ATBaseDialogFragment<VDB, VM>(resourceLayoutId: Int) : BindingDialogFragment<VDB, VM>(resourceLayoutId)
        where  VDB : ViewDataBinding, VM : BindableViewModel<*, *> {
    override val activity by lazy { getActivity() as ATBaseActivity }
    override val application by lazy { activity.application }

    internal val navController by lazy { activity.navController }

    val loginViewModel: LoginViewModel by activityViewModels { LoginViewModelFactory(lifecycle) }

    override fun getBindingVariable() = BR.viewModel

}
