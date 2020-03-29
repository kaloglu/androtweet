package com.zsk.androtweet.ui.fragments.base

import androidx.databinding.ViewDataBinding
import com.kaloglu.library.ui.viewmodel.databinding.BindableViewModel
import com.kaloglu.library.ui.viewmodel.databinding.BindingFragment
import com.zsk.androtweet.BR
import com.zsk.androtweet.ui.activities.base.ATBaseActivity

abstract class ATBaseFragment<VDB, VM>(resourceLayoutId: Int) : BindingFragment<VDB, VM>(resourceLayoutId)
        where  VDB : ViewDataBinding, VM : BindableViewModel<*, *> {

    override val activity by lazy { getActivity() as ATBaseActivity }
    override val application by lazy { activity.application }
    internal val navController by lazy { activity.navController }

    override fun getBindingVariable() = BR.viewModel

}
