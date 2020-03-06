package com.zsk.androtweet.ui.fragments.base

import androidx.databinding.ViewDataBinding
import com.kaloglu.library.ui.viewmodel.BaseViewModel
import com.kaloglu.library.ui.viewmodel.BaseViewModelFactory
import com.kaloglu.library.ui.viewmodel.databinding.BindingFragment
import com.zsk.androtweet.ui.activities.base.ATBaseActivity

abstract class ATBaseFragment<VDB : ViewDataBinding, VM : BaseViewModel<*>>(resourceLayoutId: Int) : BindingFragment<VDB, VM>(resourceLayoutId) {

    override val activity by lazy { getActivity() as ATBaseActivity }
    override val application by lazy { activity.application }
    internal val navController by lazy { activity.navController }
    internal val viewModelStoreOwner by lazy { activity.viewModelStoreOwner }
    abstract val viewModelFactory: BaseViewModelFactory
}
